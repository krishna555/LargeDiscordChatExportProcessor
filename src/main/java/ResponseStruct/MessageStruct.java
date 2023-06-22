package ResponseStruct;
import java.util.List;

public class MessageStruct {
    private String id;
    private String content;
    private AuthorStruct author;
    private String timestamp;
    private boolean pinned;
    private List<MentionStruct> mentions;
    private List<ReactionStruct> reactions;

    private List<AttachmentStruct> attachments;

    public String display() {
        return this.id + "," + this.content;
    }

    public String getContent() {
        return this.content;
    }

    public String getId() {
        return this.id;
    }

    public boolean getPinned() {return this.pinned;}

    public String getTs() {return this.timestamp;}

    public AuthorStruct getAuthor() {return this.author;}

    public List<MentionStruct> getMentions() {return this.mentions;}

    public List<ReactionStruct> getReactions() {return this.reactions;}

    public List<AttachmentStruct> getAttachments() {return this.attachments;}
 }
