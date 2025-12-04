package com.lifepill.possystem.service.impl;

import com.lifepill.possystem.client.IdentityServiceClient;
import com.lifepill.possystem.client.InventoryServiceClient;
import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceItemDTO;
import com.lifepill.possystem.dto.GroupedOrderDetails;
import com.lifepill.possystem.dto.requestDTO.RequestOrderDetailsSaveDTO;
import com.lifepill.possystem.dto.requestDTO.RequestOrderSMSDTO;
import com.lifepill.possystem.dto.requestDTO.RequestOrderSaveDTO;
import com.lifepill.possystem.dto.requestDTO.RequestPaymentDetailsDTO;
import com.lifepill.possystem.dto.responseDTO.OrderResponseDTO;
import com.lifepill.possystem.entity.Order;
import com.lifepill.possystem.entity.OrderDetails;
import com.lifepill.possystem.entity.PaymentDetails;
import com.lifepill.possystem.exception.InsufficientItemQuantityException;
import com.lifepill.possystem.exception.NotFoundException;
import com.lifepill.possystem.repo.orderRepository.OrderDetailsRepository;
import com.lifepill.possystem.repo.orderRepository.OrderRepository;
import com.lifepill.possystem.repo.paymentRepository.PaymentRepository;
import com.lifepill.possystem.service.OrderService;
import com.lifepill.possystem.util.mappers.OrderMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link OrderService} interface that handles order-related operations.
 * Uses OpenFeign clients to communicate with other microservices.
 */
@Service
@Transactional
@AllArgsConstructor
public class OrderServiceIMPL implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceIMPL.class);
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailsRepository orderDetailsRepo;
    private final InventoryServiceClient inventoryServiceClient;
    private final IdentityServiceClient identityServiceClient;
    private final OrderDetailsRepository orderDetailsRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;


    /**
     * Adds an order to the system.
     *
     * @param requestOrderSaveDTO The DTO containing order details.
     * @return A message indicating the result of the operation.
     */
    @Override
    public String addOrder(RequestOrderSaveDTO requestOrderSaveDTO) {
        // Validate employee exists via Identity Service
        validateEmployee(requestOrderSaveDTO.getEmployerId());
        
        // Check if items in the order have sufficient quantity
        checkItemStock(requestOrderSaveDTO);

        // Update item quantities
        updateItemQuantities(requestOrderSaveDTO);

        Order order = new Order();
        order.setEmployerId(requestOrderSaveDTO.getEmployerId());
        order.setOrderDate(requestOrderSaveDTO.getOrderDate());
        order.setTotal(requestOrderSaveDTO.getTotal());
        order.setBranchId(requestOrderSaveDTO.getBranchId());
        orderRepository.save(order);

        if (orderRepository.existsById(order.getOrderId())) {
            List<OrderDetails> orderDetails = modelMapper.
                    map(requestOrderSaveDTO.getOrderDetails(), new TypeToken<List<OrderDetails>>() {
                            }
                                    .getType()
                    );
            for (int i = 0; i < orderDetails.size(); i++) {
                orderDetails.get(i).setOrders(order);
                orderDetails.get(i).setItemId(requestOrderSaveDTO
                                .getOrderDetails().get(i).getId()
                );
            }
            if (!orderDetails.isEmpty()) {
                orderDetailsRepo.saveAll(orderDetails);
            }
            savePaymentDetails(requestOrderSaveDTO.getPaymentDetails(), order);
            return "saved";
        }

        return "Order saved";
    }

    @Override
    public String addOrderWithSMS(RequestOrderSMSDTO requestOrderSaveDTO) {
        // Validate employee exists via Identity Service
        validateEmployee(requestOrderSaveDTO.getEmployerId());
        
        // Check if items in the order have sufficient quantity
        checkItemStockSMS(requestOrderSaveDTO);
        // Update item quantities
        updateItemQuantitiesSMS(requestOrderSaveDTO);

        String customerEmail = requestOrderSaveDTO.getCustomerEmail();

        Order order = new Order();
        order.setEmployerId(requestOrderSaveDTO.getEmployerId());
        order.setOrderDate(requestOrderSaveDTO.getOrderDate());
        order.setTotal(requestOrderSaveDTO.getTotal());
        order.setBranchId(requestOrderSaveDTO.getBranchId());

        orderRepository.save(order);

        if (orderRepository.existsById(order.getOrderId())) {
            List<OrderDetails> orderDetails = modelMapper
                    .map(requestOrderSaveDTO.getOrderDetails(), new TypeToken<List<OrderDetails>>() {
                            }
                                    .getType()
                    );
            for (int i = 0; i < orderDetails.size(); i++) {
                orderDetails.get(i).setOrders(order);
                orderDetails.get(i).setItemId(requestOrderSaveDTO
                                .getOrderDetails().get(i).getId()
                );
            }
            if (!orderDetails.isEmpty()) {
                orderDetailsRepo.saveAll(orderDetails);
            }
            System.out.println("Payment Details: " + requestOrderSaveDTO.getPaymentDetails());
            log.info("Payment Details: " + requestOrderSaveDTO.getPaymentDetails());
            savePaymentDetails(requestOrderSaveDTO.getPaymentDetails(), order);
            log.info("requestOrderSaveDTO.getCustomerPhoneNumber(): " + requestOrderSaveDTO.getCustomerPhoneNumber());
            log.info("order: " + order);
//            sendOrderDetailsSms(requestOrderSaveDTO.getCustomerPhoneNumber(), order);

            sendOrderConfirmationEmail(customerEmail, order);
            return "saved";
        }

        return "Order saved";

    }

    private void sendOrderConfirmationEmail(String customerEmail, Order order) {
        StringBuilder message = new StringBuilder();
        message.append("Thank you for your order!\n\n");
        message.append("Order ID: ").append(order.getOrderId()).append("\n");
        message.append("Date: ").append(order.getOrderDate()).append("\n");
        message.append("Total: ").append(order.getTotal()).append("\n\n");
        message.append("Items:\n");

        // Check if order.getOrderDetails() is not null
        if (order.getOrderDetails() != null) {
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                message.append(orderDetails.getName()).append(" - ").append(orderDetails.getAmount()).append("\n");
            }
        } else {
            // Handle the case when order.getOrderDetails() is null
            message.append("No order details found.");
        }

      //  emailService.sendEmail(customerEmail, "Your Order Confirmation", message.toString());
    }

    /**
     * Validates that an employee exists via Identity Service.
     *
     * @param employerId The ID of the employee to validate.
     * @throws NotFoundException if the employee is not found.
     */
    private void validateEmployee(Long employerId) {
        try {
            ResponseEntity<MicroserviceApiResponse<Boolean>> response = 
                identityServiceClient.employeeExists(employerId);
            if (response.getBody() != null && response.getBody().isSuccess() 
                && !Boolean.TRUE.equals(response.getBody().getData())) {
                throw new NotFoundException("Employee not found with ID: " + employerId);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Could not verify employee from Identity Service for ID: {}. Proceeding anyway.", employerId);
        }
    }

    private void sendOrderDetailsSms(String customerPhoneNumber, Order order) {
        StringBuilder message = new StringBuilder();
        message.append("Thank you for your order!\n\n");
        message.append("Order ID: ").append(order.getOrderId()).append("\n");
        message.append("Date: ").append(order.getOrderDate()).append("\n");
        message.append("Total: ").append(order.getTotal()).append("\n\n");
        message.append("Items:\n");

        // Check if order.getOrderDetails() is not null
        if (order.getOrderDetails() != null) {
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                message.append(orderDetails.getName()).append(" - ").append(orderDetails.getAmount()).append("\n");
            }
        } else {
            // Handle the case when order.getOrderDetails() is null
            message.append("No order details found.");
        }

//        smsService.sendSms(customerPhoneNumber, message.toString());
    }

    /**
     * Saves payment details for an order.
     *
     * @param paymentDetailsDTO The DTO containing payment details.
     * @param order             The order for which the payment is made.
     */
    private void savePaymentDetails(RequestPaymentDetailsDTO paymentDetailsDTO, Order order) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentMethod(paymentDetailsDTO.getPaymentMethod());
        paymentDetails.setPaymentAmount(paymentDetailsDTO.getPaymentAmount());
        paymentDetails.setPaymentDate(paymentDetailsDTO.getPaymentDate());
        paymentDetails.setPaymentNotes(paymentDetailsDTO.getPaymentNotes());
        paymentDetails.setPaymentDiscount(paymentDetailsDTO.getPaymentDiscount());
        paymentDetails.setPaidAmount(paymentDetailsDTO.getPayedAmount());
        paymentDetails.setOrders(order); // Set the order for which this payment is made
        paymentRepository.save(paymentDetails);
    }

    /**
     * Checks the stock availability of items in the order via Inventory Service.
     *
     * @param requestOrderSaveDTO The DTO containing the order details.
     * @throws InsufficientItemQuantityException if an item in the order does not have enough quantity.
     * @throws NotFoundException                 if an item in the order is not found in the service.
     */
    private void checkItemStock(RequestOrderSaveDTO requestOrderSaveDTO) {
        for (RequestOrderDetailsSaveDTO orderDetail : requestOrderSaveDTO.getOrderDetails()) {
            try {
                var response = inventoryServiceClient.getItemById(orderDetail.getId());
                if (response != null && response.getBody() != null && 
                    response.getBody().isSuccess() && response.getBody().getData() != null) {
                    MicroserviceItemDTO item = response.getBody().getData();
                    if (item.getItemQuantity() < orderDetail.getAmount()) {
                        throw new InsufficientItemQuantityException(
                                "Item " + item.getItemId()
                                        + " does not have enough quantity"
                        );
                    }
                } else {
                    throw new NotFoundException("Item not found with ID: " + orderDetail.getId());
                }
            } catch (InsufficientItemQuantityException | NotFoundException e) {
                throw e;
            } catch (Exception e) {
                log.warn("Could not verify item stock from Inventory Service for item: {}", orderDetail.getId());
            }
        }
    }

    private void checkItemStockSMS(RequestOrderSMSDTO requestOrderSaveDTO) {
        for (RequestOrderDetailsSaveDTO orderDetail : requestOrderSaveDTO.getOrderDetails()) {
            try {
                var response = inventoryServiceClient.getItemById(orderDetail.getId());
                if (response != null && response.getBody() != null && 
                    response.getBody().isSuccess() && response.getBody().getData() != null) {
                    MicroserviceItemDTO item = response.getBody().getData();
                    if (item.getItemQuantity() < orderDetail.getAmount()) {
                        throw new InsufficientItemQuantityException(
                                "Item " + item.getItemId()
                                        + " does not have enough quantity"
                        );
                    }
                } else {
                    throw new NotFoundException("Item not found with ID: " + orderDetail.getId());
                }
            } catch (InsufficientItemQuantityException | NotFoundException e) {
                throw e;
            } catch (Exception e) {
                log.warn("Could not verify item stock from Inventory Service for item: {}", orderDetail.getId());
            }
        }
    }


    /**
     * Updates the quantities of items via Inventory Service after an order is placed.
     *
     * @param requestOrderSaveDTO The DTO containing the order details.
     * @throws NotFoundException if an item in the order is not found in the service.
     */
    private void updateItemQuantities(RequestOrderSaveDTO requestOrderSaveDTO) {
        for (RequestOrderDetailsSaveDTO orderDetail : requestOrderSaveDTO.getOrderDetails()) {
            try {
                // Deduct the ordered quantity from inventory
                inventoryServiceClient.updateStock(orderDetail.getId(), -orderDetail.getAmount());
            } catch (Exception e) {
                log.warn("Could not update stock via Inventory Service for item: {}", orderDetail.getId());
            }
        }
    }

    private void updateItemQuantitiesSMS(RequestOrderSMSDTO requestOrderSaveDTO) {
        for (RequestOrderDetailsSaveDTO orderDetail : requestOrderSaveDTO.getOrderDetails()) {
            try {
                // Deduct the ordered quantity from inventory
                inventoryServiceClient.updateStock(orderDetail.getId(), -orderDetail.getAmount());
            } catch (Exception e) {
                log.warn("Could not update stock via Inventory Service for item: {}", orderDetail.getId());
            }
        }
    }

    /**
     * Retrieves all orders with their details.
     *
     * @return A list of OrderResponseDTO containing orders with details.
     */
    public List<OrderResponseDTO> getAllOrdersWithDetails() {
        List<Order> orders = orderRepository.findAll();
        Map<String, List<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate() + "-"
                                + order.getBranchId() + "-"
                                + order.getEmployerId()
                ));

        return groupedOrders.entrySet().stream()
                .map(entry -> {
                    List<Order> ordersInGroup = entry.getValue();
                    Order firstOrder = ordersInGroup.get(0);

                    OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
                    orderResponseDTO.setEmployerId(firstOrder.getEmployerId());
                    orderResponseDTO.setBranchId(firstOrder.getBranchId());
                    orderResponseDTO.setOrderDate(firstOrder.getOrderDate());
                    orderResponseDTO.setTotal(ordersInGroup.stream().mapToDouble(Order::getTotal).sum());

                    List<RequestOrderDetailsSaveDTO> orderDetails = ordersInGroup.stream()
                            .flatMap(order -> order.getOrderDetails().stream())
                            .map(orderDetail -> {
                                RequestOrderDetailsSaveDTO dto = modelMapper.map(orderDetail, RequestOrderDetailsSaveDTO.class);
                                dto.setId(firstOrder.getOrderDetails().iterator().next().getItemId()); // Ensure the ID is set correctly
                                return dto;
                            })
                            .collect(Collectors.toList());

                    // Limit orderDetails to the actual number of orders in the group
                    //    orderDetails = orderDetails.stream().limit(ordersInGroup.size()).collect(Collectors.toList());

                    RequestPaymentDetailsDTO paymentDetails = ordersInGroup.stream()
                            .filter(order -> order.getPaymentDetails() != null && !order.getPaymentDetails().isEmpty())
                            .map(order -> modelMapper.map(
                                    order.getPaymentDetails().iterator().next(), // Get the first payment detail
                                    RequestPaymentDetailsDTO.class)
                            )
                            .findFirst()
                            .orElse(null);

                    if (paymentDetails != null && !ordersInGroup.get(0).getPaymentDetails().isEmpty()) {
                        paymentDetails.setPayedAmount(firstOrder.getPaymentDetails().iterator().next().getPaidAmount());
                    }

                    int orderCount = ordersInGroup.size();

                    // Set these values to orderResponseDTO object
                    orderResponseDTO.setGroupedOrderDetails(
                            new GroupedOrderDetails(orderDetails, paymentDetails, orderCount)
                    );

                    Long itemId = firstOrder.getOrderDetails().iterator().next().getItemId();
                    System.out.println(" item id: " + itemId);
                    // Logging statements to debug and verify values
                    System.out.println("Order ID: " + firstOrder.getOrderId());
                    System.out.println("First Order Details ID: " + firstOrder.getOrderDetails().iterator().next().getOrderDetailsId());
                    System.out.println("First Order Payed Amount: " + firstOrder.getPaymentDetails().iterator().next().getPaidAmount());

                    return orderResponseDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order with its details by the provided order ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The order response DTO containing the order details.
     * @throws NotFoundException if no order is found with the provided ID.
     */
    public OrderResponseDTO getOrderWithDetailsById(long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        System.out.println("Order ID: " + orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return orderMapper.mapOrderToResponseDTO(order);
        } else {
            throw new NotFoundException("Order not found with ID: " + orderId);
        }
    }

    /**
     * Retrieves orders with their details by the provided branch ID.
     *
     * @param branchId The ID of the branch to retrieve orders from.
     * @return A list of OrderResponseDTO containing orders with details.
     * @throws NotFoundException if no orders are found for the provided branch ID.
     */
    public List<OrderResponseDTO> getOrderWithDetailsByBranchId(long branchId) {
        List<Order> orders = orderRepository.findByBranchId(branchId);
        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found for branch ID: " + branchId);
        }

        Map<String, List<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate() + "-"
                                + order.getBranchId() + "-"
                                + order.getEmployerId()
                ));

        return groupedOrders.entrySet().stream()
                .map(entry -> {
                    List<Order> ordersInGroup = entry.getValue();
                    Order firstOrder = ordersInGroup.get(0);

                    OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
                    orderResponseDTO.setEmployerId(firstOrder.getEmployerId());
                    orderResponseDTO.setBranchId(firstOrder.getBranchId());
                    orderResponseDTO.setOrderDate(firstOrder.getOrderDate());
                    orderResponseDTO.setTotal(ordersInGroup.stream().mapToDouble(Order::getTotal).sum());

                    List<RequestOrderDetailsSaveDTO> orderDetails = ordersInGroup.stream()
                            .flatMap(order -> order.getOrderDetails().stream())
                            .map(orderDetail -> {
                                RequestOrderDetailsSaveDTO dto = modelMapper.map(orderDetail, RequestOrderDetailsSaveDTO.class);
                                dto.setId(orderDetail.getItemId()); // Ensure the ID is set correctly
                                return dto;
                            })
                            .collect(Collectors.toList());

                    RequestPaymentDetailsDTO paymentDetails = ordersInGroup.stream()
                            .filter(order -> order.getPaymentDetails() != null && !order.getPaymentDetails().isEmpty())
                            .map(order -> modelMapper.map(
                                    order.getPaymentDetails().iterator().next(), // Get the first payment detail
                                    RequestPaymentDetailsDTO.class)
                            )
                            .findFirst()
                            .orElse(null);

                    if (paymentDetails != null && !firstOrder.getPaymentDetails().isEmpty()) {
                        paymentDetails.setPayedAmount(firstOrder.getPaymentDetails().iterator().next().getPaidAmount());
                    }

                    int orderCount = ordersInGroup.size();

                    orderResponseDTO.setGroupedOrderDetails(
                            new GroupedOrderDetails(orderDetails, paymentDetails, orderCount)
                    );

                    return orderResponseDTO;
                })
                .collect(Collectors.toList());
    }
}
