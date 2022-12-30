package shop.kubanmebel.kubanmebel_bot.bot.constants;

public enum CommandNameEnum {
    START("/start"),
    NEW("/new"),
    CONTACTS("/contacts"),
    AUTHOR("/author");

    private final String commandName;

    CommandNameEnum(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
