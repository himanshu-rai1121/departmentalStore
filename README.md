# Departmental Store Management System

Welcome to our Departmental Store Management System! This repository houses the codebase for a robust system designed to efficiently manage customers, products, orders, and discounts. Below, you'll find an overview of the system's features and guidelines for using it effectively.

## Features

1. **Customer Management**: Easily create, update, and manage customer records within the system.
2. **Product Management**: Seamlessly handle products, including creation, updating, and deletion. The system keeps track of product quantities and backorders.
3. **Discount Management**: Create and apply discounts to orders as needed, enhancing flexibility in pricing strategies.
4. **Order Management**: Efficiently manage orders, including creation, updates, and deletion. The system dynamically handles order fulfillment and backorders based on product availability.
5. **Backorder Handling**: If an order exceeds available stock, the system automatically generates a backorder, ensuring no sales are lost due to inventory issues and sends notification once the product is available.
6. **Order Updates**: Easily modify existing orders with checks for quantity changes, ensuring accurate updates to product quantities and backorder status.
7. **Order Deletion**: Deleting an order triggers an increase in product quantity and resolves any associated backorders.

## Guidelines

- **Order Creation**: Prior to order creation, the system validates product availability and stock. If products are available, the order is processed; otherwise, a backorder is generated.
- **Order Deletion**: Deleting an order triggers an increase in product quantity and resolves associated backorders if products become available.
- **Product Updates**: Product quantities can be adjusted, with the system automatically checking for backorder fulfillment if quantities are increased.
- **Exception Handling**: The system throws exceptions for invalid operations, such as attempting to update a product quantity without changes or reducing a product quantity without sufficient availability.
- **Notifications**: Notifications are sent to relevant parties when backorders are resolved or when orders are updated (not implemented working code to send notification).

## Database Schema
<img src="https://github.com/GEM-himanshu-kumar/departmentalStore/assets/167817739/cf820c06-7fd6-4341-b92c-54df50487e2f" alt="Database Schema" width="800px">

## Usage

1. Clone this repository to your local machine.
2. Set up the database and configure database connections according to the provided schema.
3. Install necessary dependencies.
4. Run the application and start managing your inventory efficiently.

## Technologies Used

- **Programming Language**: Java
- **Framework**: Spring Boot
- **Build Tool**: Maven
- **Database**: MySQL (or any other relational database system (e.g., MySQL, PostgreSQL) to store and manage customer, product, order, backorder, and discount data.)
- **Other Dependencies**: Some more dependencies added in pom.xml.

## Working

### Order Management:

- If the ordered quantity exceeds the quantity left in stock, a backorder is created.
- Deleting an order results in an increase in product quantity.
- When updating an order:
  - If the product quantity remains unchanged, an exception is thrown to indicate that the quantity has not been updated.
  - If the product quantity is increased:
    - Update the product quantity.
    - Check if any backorders can be fulfilled:
      - If fulfilled, resolve the backorder and send a notification.
  - If the product quantity is decreased:
    - Update the product quantity.
    - Check if more product is available to fulfill the order:
      - If yes, update the product and order (update the amount as well).
      - If not, an exception is thrown to indicate that the quantity has not been updated because updated product quantity is more then the quantity left in the stock.
- Only the product quantity can be updated for an order; nothing else.

### Order Creation:

- Check product availability and stock.
  - If available, decrease the product quantity in product repository, calculate the amount, apply any discounts, and save the order.
  - If not available, create a backorder.

### Order Deletion:

- Deleting an order means increasing the product quantity.
  - Update the product quantity.
  - Check if any backorders can be fulfilled:
    - If fulfilled, resolve the backorder and send a notification.

### Update Product:

- Product quantity may increase or decrease.
  - If decreased, simply update the product.
  - If increased:
    - Update the product quantity.
    - Check if any backorders can be fulfilled:
      - If fulfilled, resolve the backorder and send a notification.

