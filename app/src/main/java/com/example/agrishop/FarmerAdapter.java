package com.example.agrishop;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;

        import java.util.ArrayList;

// FarmerAdapter.java
public class FarmerAdapter extends ArrayAdapter<Farmer> {

    public FarmerAdapter(Context context, ArrayList<Farmer> farmers) {
        super(context, 0, farmers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Farmer farmer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_farmer, parent, false);
        }

        TextView farmerNameTextView = convertView.findViewById(R.id.farmerNameTextView);
        TextView phoneNumberTextView = convertView.findViewById(R.id.phoneNumberTextView);
        TextView fertilizerTextView = convertView.findViewById(R.id.fertilizerTextView);
        Button payNowButton = convertView.findViewById(R.id.payNowButton);

        farmerNameTextView.setText(farmer.getName());
        phoneNumberTextView.setText(farmer.getPhoneNumber());
        fertilizerTextView.setText(farmer.getSelectedFertilizer());

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPaymentSMS(farmer.getPhoneNumber());
            }
        });

        return convertView;
    }

    private void sendPaymentSMS(String phoneNumber) {
        // Replace phoneNumber with the actual phone number
        String message = "Hello, I'm interested in purchasing from your farm. Could you please provide me with more information on availability and pricing?";
        // Create an intent with the ACTION_SENDTO action
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));  // Specify the phone number to send the SMS to
        intent.putExtra("sms_body", message);  // Specify the message body

        // Check if there is an app to handle the intent
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(intent);
        } else {
            // Handle case where no app can handle the intent
            showToast("No app to handle SMS");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

