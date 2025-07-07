package com.checkproof.explore.ai_tool_java_copilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.application.name:ai-tool-java-copilot}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        createServer("http://localhost:" + serverPort, "Local Development Server"),
                        createServer("https://api.checkproof.com", "Production Server"),
                        createServer("https://staging-api.checkproof.com", "Staging Server")
                ))
                .tags(List.of(
                        createTag("Tasks", "Task management operations including creation, retrieval, and filtering"),
                        createTag("Health", "Application health and monitoring endpoints"),
                        createTag("System", "System-level operations and utilities")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("AI Tool Java Copilot - Task Management API")
                .description("""
                        A comprehensive task management system with advanced filtering, pagination, and event-driven architecture.
                        
                        ## Features
                        - **Task Management**: Create, update, and retrieve tasks with rich metadata
                        - **Advanced Filtering**: Filter tasks by status, date ranges, and other criteria
                        - **Pagination & Sorting**: Efficient data retrieval with customizable pagination
                        - **Event-Driven**: NATS-based event processing for real-time updates
                        - **Recurrence Patterns**: Support for recurring tasks with flexible scheduling
                        - **Participant Management**: Associate participants with tasks and roles
                        
                        ## Authentication
                        Currently, this API does not require authentication. In production, implement proper security measures.
                        
                        ## Rate Limiting
                        No rate limiting is currently implemented. Consider adding rate limiting for production use.
                        
                        ## Error Handling
                        The API returns standard HTTP status codes and detailed error messages in JSON format.
                        """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Checkproof Development Team")
                        .email("dev@checkproof.com")
                        .url("https://checkproof.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private Server createServer(String url, String description) {
        return new Server()
                .url(url)
                .description(description);
    }

    private Tag createTag(String name, String description) {
        return new Tag()
                .name(name)
                .description(description);
    }
}
