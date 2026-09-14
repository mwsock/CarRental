# CarRental 

A project based on a simplified ports and adapters architecture, founded on Domain-Driven Design principles.

The main assumption is the encapsulation of business logic behind a facade and the separation of the infrastructure part
behind ports, so that they can be easily modified if needed and for test simplification.

The project requires further work — subdomain facades (Vehicle and Reservation) expose their Value Objects and the logic
contained within services. In the target solution, the logic would be hidden behind the facade, and communication with
the facade would take place via logic-free DTOs. As a result, this would maintain domain separation while providing a
strong contract for their use. Simultaneously, this should allow for precise tests verifying the application's business
assumptions without delving into implementation details.

The project does not have a framework implementation (ultimately Spring Boot) due to its goal — showcasing the
architecture principles and the adopted design assumptions.

The desired effect is a business domain — the application core — free from the framework and based as much as possible
on pure Java, which in effect helps prevent mixing business logic with technicalities and aids in maintenance.