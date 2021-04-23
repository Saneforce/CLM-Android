package saneforce.sanclm.activities;

import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import saneforce.sanclm.R;

public class PdfViewActivity extends AppCompatActivity {
    PDFView pdfView;
    ImageView img_del;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfView=(PDFView)findViewById(R.id.pdfView);
        img_del=(ImageView)findViewById(R.id.img_del);
        Bundle extra=getIntent().getExtras();
        String slideUrl=extra.getString("PdfUrl");
        Log.v("pdf_file_to_view",slideUrl);
        pdfView.setVisibility(View.VISIBLE);
        pdfView.fromUri(Uri.parse("file://"+slideUrl))
                // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(false)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                // add dynamic spacing to fit each page on its own on the screen
                // toggle night mode
                .load();

        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
