package rdoshi.codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        final EditText mEditText = (EditText) findViewById(R.id.etEditItem);

        final int position = getIntent().getExtras().getInt("position");
        String item = getIntent().getExtras().getString("item");

        mEditText.append(item);

        findViewById(R.id.btnSaveItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();

                data.putExtra("item", mEditText.getText().toString());
                data.putExtra("position", position);

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
