# microservices-training
a definition of this new architectural term

The term "Microservice Architecture" has sprung up over the last few years to describe a particular way of designing software applications as suites of independently deployable services.

In short, the microservice architectural style is an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often an HTTP resource API. These services are built around business capabilities and independently deployable by fully automated deployment machinery. There is a bare minimum of centralized management of these services, which may be written in different programming languages and use different data storage technologies
# What are microservices?
Microservices - also known as the microservice architecture - is an architectural style that structures an application as a collection of services that are

Highly maintainable and testable
Loosely coupled
Independently deployable
Organized around business capabilities
Owned by a small team
The microservice architecture enables the rapid, frequent and reliable delivery of large, complex applications. It also enables an organization to evolve its technology stack.

A good starting point is the Monolithic Architecture pattern, which is the traditional architectural style that is still a good choice for many applications. It does, however, have numerous limitations and issues and so a better choice for large/complex applications is the Microservice architecture pattern

# Monolithic Architecture
# Microservice Architeture

# Service Registration
Registration server called Eureka (“I have found it” in Greek). Spring Cloud also supports **Consul** as an alternative to Eureka
Code(Source from [https://spring.io/blog/2015/07/14/microservices-with-spring]

@SpringBootApplication

@EnableEurekaServer

public class ServiceRegistrationServer {


  public static void main(String[] args) {
  
    // Tell Boot to look for registration-server.yml
    
    System.setProperty("spring.config.name", "registration-server");
    
    SpringApplication.run(ServiceRegistrationServer.class, args);
    
  
  }
}

- By default Spring Boot applications look for an **application.propertie**s or **application.yml** file for configuration. By setting the **spring.config.name** property we can tell Spring Boot to look for a different file 
