package ResponseStruct;
public class MentionStruct {
    private String id;
    private String name;
    private String discriminator;
    private String nickname;
    private String color;
    private String isBot;
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getColor() {
        return color;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getId() {
        return id;
    }

    public String getIsBot() {
        return isBot;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }
}
