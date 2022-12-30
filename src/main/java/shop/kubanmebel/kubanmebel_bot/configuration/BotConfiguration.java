package shop.kubanmebel.kubanmebel_bot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@PropertySource("application.properties")
@Data
public class BotConfiguration {
    @Value(value = "${bot.name}")
    private String userName;
    @Value(value = "${bot.token}")
    private String token;
}
