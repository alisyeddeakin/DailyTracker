package deakin.com.example.healthbot.Common;


public class Enums {

    public enum ApiTask {

        NONE(""),
        LOGIN("login.php?"),
        FACEBOOK_LOGIN("facebook_login.php?"),
        SIGN_UP("signup.php?"),
        ACCOUNT_VERIFICATION("verification.php?"),
        REGISTER_DEVICE("gcm_id.php?"),
        
        CHAT_MAIN("chatt_main.php?"),
        CHAT_DETAIL("chat_detail.php?"),
        SEND_MESSAGE("add_message.php?"),
        DELETE_MESSAGE("delete_message.php?"),
        DELETE_INBOX("delete_inbox.php?"),
        
        FIND_POONDI("moment_places.php?"),
        
        UPDATE_USERNAME("update_username.php?"),
        UPDATE_ACTIVITY_STATUS("update_activity_status.php?"),
        UPDATE_LOCATION("update_latlng.php?"),
        UPDATE_PRIVACY("update_privacy.php?"),
        
        FIND_NEAR_BY("nearby_users.php?"),
        
        LOAD_CONTACTS("load_contacts.php?"),
        SYNC_PHONE_CONTACTS("sync_phone_contacts.php?"),
        SYNC_FB_CONTACTS("sync_fb_contacts.php?"),
        SEND_FRIEND_REQUEST("send_friend_request.php?"),
        CAN_FRIEND_REQUEST("can_friend_request.php?"),
        ACCEPT_FRIEND_REQUEST("accept_friend_request.php?"),
        REJECT_FRIEND_REQUEST("reject_friend_request.php?"),
        LOAD_FRIEND_REQUESTS("load_friend_requests.php?"),
        
        UPDATE_PHONE("update_phone.php?"),
        UPDATE_FBID("update_fbid.php?"),
        
        UPLOAD_PROFILE_IMAGE("upload_profile_image.php?"),
        UPLOAD_MOMENT("upload_moment.php?"),
        LOG_OUT("signout_user?"),
        
        PLACE_IMAGE("place_image.php?"),
        
        UPDATE_PLACE("update_location.php?"),
        GET_PLACES("get_places.php?"),
        
        MAKE_ME_ACTIVE("make_me_active.php?"),
        GET_ONLINE_STATUS("get_online_status.php?"),

        LOAD_ADS("load_ads.php?"),
        LOAD_TRENDING_CITIES("load_trending_cities.php?");

        private String task;

        ApiTask(String task) { this.task = task; }

        public String getTask() { return this.task; }

        public static ApiTask get(String task) {
            String task_ = Utils.getValid(task);
            if(Utils.isValid(task_)) {
                for (ApiTask type_ : ApiTask.values()) {
                    if (type_.getTask().equals(task_)) { return type_; }
                }
            }
            return ApiTask.NONE;
        }
    }
}
