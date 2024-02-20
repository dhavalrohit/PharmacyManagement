package com.example.pharmacure.sample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacure.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

public class sampleactivity extends AppCompatActivity {
    Button btnPrint, btnPrintMultiPage, btnInvoice, btnDemoBarcode, btnPrintDemoList,
            btnPrintMultiPageDynamic, btnPrintHorizontalScrollView, btnPrintLandscape;
    private Fragment demoInvoiceFragment;
    private PdfGenerator.XmlToPDFLifecycleObserver xmlToPDFLifecycleObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.samplelayout);
        btnPrint = findViewById(R.id.bt_print);
        btnPrintMultiPage = findViewById(R.id.bt_print_multi_page);
        btnInvoice = findViewById(R.id.bt_invoice);
        btnDemoBarcode = findViewById(R.id.bt_demo_barcode);
        btnPrintDemoList = findViewById(R.id.bt_print_list);
        btnPrintMultiPageDynamic = findViewById(R.id.bt_print_multi_page_dynamic);
        btnPrintHorizontalScrollView = findViewById(R.id.bt_horizontal_scroll_view);
        btnPrintLandscape = findViewById(R.id.bt_print_landscape);


        xmlToPDFLifecycleObserver = new PdfGenerator.XmlToPDFLifecycleObserver(this);
        getLifecycle().addObserver(xmlToPDFLifecycleObserver);


        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * MUST NEED TO set android:layout_width A FIXED
                 * VALUE INSTEAD OF "wrap_content" and "match_parent" OTHERWISE SIZING COULD BE MALFORMED
                 * IN PDF FOR DIFFERENT DEVICE SCREEN
                 */

                PdfGenerator.getBuilder()
                        .setContext(sampleactivity.this)
                        .fromViewIDSource()
                        .fromViewID(sampleactivity.this, R.id.tv_print_area)
                        .setFileName("Demo-Text")
                        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
                        .savePDFSharedStorage(xmlToPDFLifecycleObserver)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }

                            @Override
                            public void onStartPDFGeneration() {

                            }

                            @Override
                            public void onFinishPDFGeneration() {

                            }

                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                                Log.d("PDF-generation",log);
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }
                        });
            }
        });


        btnPrintLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * MUST NEED TO set android:layout_width A FIXED
                 * VALUE INSTEAD OF "wrap_content" and "match_parent" OTHERWISE SIZING COULD BE MALFORMED
                 * IN PDF FOR DIFFERENT DEVICE SCREEN
                 */

                PdfGenerator.getBuilder()
                        .setContext(sampleactivity.this)
                        .fromViewIDSource()
                        .fromViewID(sampleactivity.this, R.id.tv_print_area)
                        .setFileName("Demo-Landscape")
                        .setFolderNameOrPath("MyFolder/MyDemoLandscape/")
                        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }

                            @Override
                            public void onStartPDFGeneration() {

                            }

                            @Override
                            public void onFinishPDFGeneration() {

                            }

                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }
                        });
            }
        });


        btnPrintMultiPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * MUST NEED TO set android:layout_width A FIXED
                 * VALUE INSTEAD OF "wrap_content" and "match_parent" OTHERWISE SIZING COULD BE MALFORMED
                 * IN PDF FOR DIFFERENT DEVICE SCREEN
                 */

                PdfGenerator.getBuilder()
                        .setContext(sampleactivity.this)
                        .fromViewIDSource()
                        .fromViewID(sampleactivity.this,
                                R.id.tv_print_area, R.id.tv_print_area, R.id.tv_print_area)
                        .setFileName("Demo-Text-Multi-Page")
                        .setFolderNameOrPath("MyFolder/MyDemoTextMultiPage/")
                        .savePDFSharedStorage(xmlToPDFLifecycleObserver)
                        .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }

                            @Override
                            public void onStartPDFGeneration() {

                            }

                            @Override
                            public void onFinishPDFGeneration() {

                            }

                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }
                        });
            }
        });
        btnPrintMultiPageDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(v.getContext());

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Input dynamic text")
                        .setMessage("Please input your demo text. It will populate a multi " +
                                "paged pdf with your demo text")
                        .setView(editText)
                        .setPositiveButton("Input", (dialog, whichButton) -> {
                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            /**
                             * MUST NEED TO set android:layout_width A FIXED
                             * VALUE INSTEAD OF "wrap_content" and "match_parent" OTHERWISE SIZING COULD BE MALFORMED
                             * IN PDF FOR DIFFERENT DEVICE SCREEN
                             */
                            View content = inflater.inflate(R.layout.activity_main, null);
                            TextView tvDynamicText = content.findViewById(R.id.tv_print_area);
                            tvDynamicText.setText(editText.getText().toString());

                            PdfGenerator.getBuilder()
                                    .setContext(sampleactivity.this)
                                    .fromViewSource()
                                    .fromView(tvDynamicText, tvDynamicText, tvDynamicText)
                                    .setFileName("Demo-Text-Multi-Page-With-Dynamic-Text")
                                    .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.SHARE)
                                    .build(new PdfGeneratorListener() {
                                        @Override
                                        public void onFailure(FailureResponse failureResponse) {
                                            super.onFailure(failureResponse);
                                        }

                                        @Override
                                        public void onStartPDFGeneration() {

                                        }

                                        @Override
                                        public void onFinishPDFGeneration() {

                                        }

                                        @Override
                                        public void showLog(String log) {
                                            super.showLog(log);
                                        }

                                        @Override
                                        public void onSuccess(SuccessResponse response) {
                                            super.onSuccess(response);
                                        }
                                    });
                        })
                        .setNegativeButton("Cancel", (dialog, whichButton) -> {
                            dialog.dismiss();
                        })
                        .show();

            }
        });




        btnInvoice.setOnClickListener(v -> {
            if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, demoInvoiceFragment)
                        .commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().beginTransaction().remove(demoInvoiceFragment).commit();
    }
}