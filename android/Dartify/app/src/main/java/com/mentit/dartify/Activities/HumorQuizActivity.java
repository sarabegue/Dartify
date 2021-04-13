package com.mentit.dartify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Adapters.HumorAdapter;
import com.mentit.dartify.HelperClasses.OnStartDragInterface;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.HelperClasses.SimpleItemTouchHelperCallback;
import com.mentit.dartify.Models.Chiste;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Test.PutHumorTestTask;
import com.mentit.dartify.util.Valores;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class HumorQuizActivity extends AppCompatActivity implements OnStartDragInterface, PutHumorTestTask.OnTaskCompleted {
    private Button buttonEndHumorQuiz;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager lmanager;
    private HumorAdapter madapter;

    private long buttonLastTimeClick = 0;

    private long userid;

    private ItemTouchHelper mItemTouchHelper;

    ArrayList<Chiste> listaChistes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_humor_quiz);
        buttonEndHumorQuiz = findViewById(R.id.buttonEndHumorQuiz);
        recyclerView = findViewById(R.id.recyclerViewHumor);

        Bundle b = getIntent().getExtras();
        listaChistes = (ArrayList<Chiste>) b.getSerializable("lista");

        lmanager = new LinearLayoutManager(this);
        madapter = new HumorAdapter(listaChistes, R.layout.cardview_humor, nClickListener, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(lmanager);

        recyclerView.setAdapter(madapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(madapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        buttonEndHumorQuiz.setOnClickListener(clicklistener);

        userid = SharedPreferenceUtils.getInstance(this).getLongValue(this.getString(R.string.user_id), 0);
    }

    private HumorAdapter.OnItemClickListener nClickListener = (name, position) -> {
        //Log.d("CLICK EN ELEMENTO", position + "");
    };

    private View.OnClickListener clicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
            buttonLastTimeClick = SystemClock.elapsedRealtime();

            ArrayList<String> lista = new ArrayList();
            for (int index = 0; index < madapter.getItemCount(); index++) {
                Chiste c = madapter.getItem(index);
                lista.add(c.getCategoria() + "");
            }

            new PutHumorTestTask(HumorQuizActivity.this, userid, lista).execute("");
        }
    };

    @Override
    public void OnTaskCompletedPutHumor() {
        Thread thread = new Thread() {
            public void run() {
                runOnUiThread(() -> Toasty.success(HumorQuizActivity.this, "¡Muy bien!", Toast.LENGTH_LONG).show());
            }
        };
        thread.start();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
