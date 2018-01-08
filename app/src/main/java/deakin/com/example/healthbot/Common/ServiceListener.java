package deakin.com.example.healthbot.Common;




public interface ServiceListener {
	void onResponse(Enums.ApiTask task, boolean status, String message, Object... args);
}