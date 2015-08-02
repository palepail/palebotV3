package bot;

import java.util.List;

/**
 * Created by palepail on 8/1/2015.
 */
public class TwitchUsers {
        public _links links;
        public int chatter_count;

    public int getChatter_count() {
        return chatter_count;
    }

    public void setChatter_count(int chatter_count) {
        this.chatter_count = chatter_count;
    }

    public Chatters getChatters() {
        return chatters;
    }

    public void setChatters(Chatters chatters) {
        this.chatters = chatters;
    }

    public Chatters chatters;

        public class Chatters {
            public List<String> moderators;
            public List<String> staff;

            public List<String> getModerators() {
                return moderators;
            }

            public void setModerators(List<String> moderators) {
                this.moderators = moderators;
            }

            public List<String> getStaff() {
                return staff;
            }

            public void setStaff(List<String> staff) {
                this.staff = staff;
            }

            public List<String> getViewers() {
                return viewers;
            }

            public void setViewers(List<String> viewers) {
                this.viewers = viewers;
            }

            public List<String> getAdmins() {
                return admins;
            }

            public void setAdmins(List<String> admins) {
                this.admins = admins;
            }

            public List<String> viewers;
            public List<String> admins;
        }

    public class _links{

    }


}
