package buddha.qqbot.message;

public interface Intents {
    int GUILDS = 1;
    int GUILD_MEMBERS = 1 << 1;
    int GUILD_MESSAGES = 1 << 9;
    int GUILD_MESSAGE_REACTIONS = 1 << 10;
    int DIRECT_MESSAGE = 1 << 12;
    int INTERACTION = 1 << 26;
    int MESSAGE_AUDIT = 1 << 27;
    int FORUMS_EVENT = 1 << 28;
    int AUDIO_ACTION = 1 << 29;
    int PUBLIC_GUILD_MESSAGES = 1 << 30;

    static int publicIntents() {
        return GUILDS | GUILD_MEMBERS | GUILD_MESSAGE_REACTIONS | DIRECT_MESSAGE | INTERACTION | MESSAGE_AUDIT | AUDIO_ACTION | PUBLIC_GUILD_MESSAGES;
    }

    static int privateIntents() {
        return GUILD_MESSAGES | FORUMS_EVENT;
    }
    static int all() {
        return GUILDS | GUILD_MEMBERS | GUILD_MESSAGES
               | GUILD_MESSAGE_REACTIONS | DIRECT_MESSAGE
               | INTERACTION | MESSAGE_AUDIT | FORUMS_EVENT
               | AUDIO_ACTION | PUBLIC_GUILD_MESSAGES;
    }

}
