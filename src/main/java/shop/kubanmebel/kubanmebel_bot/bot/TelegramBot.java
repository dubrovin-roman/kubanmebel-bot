package shop.kubanmebel.kubanmebel_bot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import shop.kubanmebel.kubanmebel_bot.bot.constants.CommandNameEnum;
import shop.kubanmebel.kubanmebel_bot.configuration.BotConfiguration;
import shop.kubanmebel.kubanmebel_bot.parsermodule.Parser;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private List<String> commandList;
    private BotConfiguration botConfiguration;
    private Parser parser;

    private CommandHandler commandHandler;

    private NonCommandHandler nonCommandHandler;

    private CallbackQueryHandler callbackQueryHandler;

    public TelegramBot(BotConfiguration botConfiguration, Parser parser, CommandHandler commandHandler, NonCommandHandler nonCommandHandler, CallbackQueryHandler callbackQueryHandler) {
        this.botConfiguration = botConfiguration;
        parser.parsing();
        createMainMenu();
        this.parser = parser;
        this.commandList = createCommandList();
        this.commandHandler = commandHandler;
        this.nonCommandHandler = nonCommandHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getUserName();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (hasMessageAndTextInUpdate(update)) {
            Message message = update.getMessage();
            String textMessage = message.getText();
            if (commandList.contains(textMessage)) {
                commandHandler.setMessageData(message);
                commandHandler.setStoreData(parser.getStore());
                sendResponse(commandHandler.getResponse());
            } else {
                nonCommandHandler.setMessageData(message);
                nonCommandHandler.setStoreData(parser.getStore());
                sendResponse(nonCommandHandler.getResponse());
            }
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.setUpdateData(update);
            callbackQueryHandler.setStoreData(parser.getStore());
            sendResponse(callbackQueryHandler.getResponse());
        }
    }

    private void createMainMenu() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand(CommandNameEnum.START.getCommandName(), "Начало работы"));
        botCommandList.add(new BotCommand(CommandNameEnum.NEW.getCommandName(), "Новинки"));
        botCommandList.add(new BotCommand(CommandNameEnum.CONTACTS.getCommandName(), "Контактная информация для покупателей"));
        botCommandList.add(new BotCommand(CommandNameEnum.AUTHOR.getCommandName(), "Связь с разработчиком сайта"));
        SetMyCommands setMyCommands = new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null);
        try {
            this.execute(setMyCommands);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private boolean hasMessageAndTextInUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private List<String> createCommandList() {
        List<String> result = new ArrayList<>();
        for (CommandNameEnum commandNameEnum : CommandNameEnum.values()) {
            result.add(commandNameEnum.getCommandName());
        }
        return result;
    }

    private void sendResponse(ResponseWrapper response) {
        for (ResponseWrapper.TypeSendResponse type : response.getWhatToSend()) {
            switch (type) {
                case PHOTO:
                    response.getPhotos().forEach(this::sendPhoto);
                    break;
                case MESSAGE:
                    response.getMessages().forEach(this::sendMessage);
                    break;
                case EDIT_MESSAGE:
                    response.getEditMessages().forEach(this::sendEditMessage);
            }
        }
    }

    private void sendPhoto(SendPhoto photo) {
        try {
            this.execute(photo);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendEditMessage(EditMessageText editMessageText) {
        try {
            this.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "30 4 * * * *")
    private void updateStore() {
        parser.parsing();
        log.info("Обновление магазина выполненно.");
    }
}
