package shop.kubanmebel.kubanmebel_bot.parsermodule;

import shop.kubanmebel.kubanmebel_bot.parsermodule.store.Store;

public interface Parser {
    void parsing();
    Store getStore();
}
