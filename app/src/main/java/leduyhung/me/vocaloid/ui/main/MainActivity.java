package leduyhung.me.vocaloid.ui.main;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import leduyhung.me.vocaloid.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iMenuDrawer;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_menu_drawer:
                if (drawer.isDrawerOpen(Gravity.START)) {
                    drawer.closeDrawer(Gravity.START);
                } else {
                    drawer.openDrawer(Gravity.START);
                }
                break;
        }
    }

    private void initView() {
        iMenuDrawer = findViewById(R.id.img_menu_drawer);
        drawer = findViewById(R.id.drawer_layout);
        iMenuDrawer.setOnClickListener(this);
    }


}
