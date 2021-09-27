# Online store
The store has a catalog of Products, for which you need to implement the opportunity:
- sorting by product name (az, za)
- sorting of goods by price (from cheap to expensive, from expensive to cheap)
- sorting of goods by novelty;
- selection of goods by parameters (category, price range, color, size, etc.).
  The user browses the catalog and can add items to their cart. After adding items to the cart, a registered user can place an order. This function is not available for unregistered user. After placing an order, it (the order) is assigned the "registered" status.
  The user has a personal account where he can view his orders.
  The system administrator has the following rights:
- adding / removing goods, changing information about goods;
- blocking / unblocking the user;
- transfer of the order from the status "registered" to "paid" or "canceled".

## How to run
1)Install mysql database
2)Build and deploy the project with maven mvn spring-boot:run
3)Go to url localhost:8080/