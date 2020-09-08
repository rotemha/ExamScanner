package com.example.examscanner.components.scan_exam;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.examscanner.components.create_exam.get_version_file.PDFVersionImageImpl;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

public class BitmapsInstancesFactoryAndroidTest {
    private static final String testJpg1FilePath = "test_jpg_1.jpg";
    private static final String testJpg2FilePath = "test_jpg_2.jpg";
    private static final String testJpg3FilePath = "test_jpg_3.jpg";
    private static final String testJpg1MarkedFilePath = "test_jpg_1_marked.jpg";
    private static final String testJpg2MarkedFilePath = "test_jpg_2_marked.jpg";
    private static final String testJpg3MarkedFilePath = "test_jpg_3_marked.jpg";
    private static final String testJpgReal1 = "test_jpg_real1.jpg";
    private static final String testJpgReal2 = "test_jpg_real2.jpg";
    private static final String testJpegReal1 = "test_jpg_real1.jpeg";
    private static  final String getTestJpegDiagonal1 = "test_jpeg_diagonal1.jpeg";
    private static  final String getTestJpegBlackBackgroud = "black_back.jpg";
    private static final String testAuthPic1= "test_auth_pic_1.jpg";
    private static final String test50Qs= "exam_50_q.jpg";
    private static final String test50QsAuth= "exam_50_q_auth.jpg";
    private static String Comp191_V1_JPG_Auth_No_Flash = "comp191_v1_jpg_auth.jpg";
    private static String Comp191_V1_PDF_Auth_No_Flash = "comp191_v1_pdf_auth.jpg";;
    private static String Comp191_v1_JPG_ANS = "comp191_v1_ans.jpg";
    private static String Comp191_v1_JPG_ANS_2 = "comp191_v1_ans_jpg_2.jpg";
    private static String caspl182_V1_orig = "instances/caspl182/a1.jpg";
    private static String comp191_V1_ins_1 = "instances/comp191_v1/_1.jpg";
    private static String comp191_V1_ins_2= "instances/comp191_v1/_2.jpg";
    private static String comp191_V1_ins_3= "instances/comp191_v1/_3.jpg";
    private static String comp191_V1_ins_4= "instances/comp191_v1/_4.jpg";
    private static String comp191_V1_ins_5= "instances/comp191_v1/_5.jpg";
    private static String comp191_V1_ins_6= "instances/comp191_v1/_6.jpg";
    private static String comp191_V1_ins_7= "instances/comp191_v1/_7.jpg";
    private static String comp191_V1_ins_8= "instances/comp191_v1/_8.jpg";
    private static String comp191_V1_ins_9 ="instances/comp191_v1/_9.jpg";
    private static String comp191_V1_ins_15 = "instances/comp191_v1/_15.jpg";
    private static String comp191_V1_ins_16 = "instances/comp191_v1/_16.jpg";
    private static String comp191_V1_ins_17 = "instances/comp191_v1/_17.jpg";
    private static String comp191_V1_ins_18 = "instances/comp191_v1/_18.jpg";
    private static String comp191_V1_ins_19 = "instances/comp191_v1/_19.jpg";
    private static String comp191_V1_ins_20 = "instances/comp191_v1/_20.jpg";
    private static String comp191_V1_ins_21 = "instances/comp191_v1/_21.jpg";
    private static String comp191_V1_ins_22 = "instances/comp191_v1/_22.jpg";
    private static String comp191_V1_ins_23 = "instances/comp191_v1/_23.jpg";
    private static String comp191_V1_ins_24 = "instances/comp191_v1/_24.jpg";
    private static String comp191_V1_ins_25 = "instances/comp191_v1/_25.jpg";
    private static String comp191_V1_ins_26 = "instances/comp191_v1/_26.jpg";
    private static String caspl182_V1_ins_1 = "instances/caspl182/_1.jpg";
    private static String comp191_V1_ins_in1 = "instances/comp191_v1/in_1.jpg";
    private static String comp191_V1_pdf_ins_in1 ="instances/comp191_v1/in_1.pdf";
    private static String comp191_V1_pdf_ins_in10 = "instances/comp191_v1/_10.jpg";
    private static String comp191_V1_pdf_ins_in11 = "instances/comp191_v1/_11.jpg";
    private static String comp191_V1_pdf_ins_in12 = "instances/comp191_v1/_12.jpg";
    private static String comp191_V1_pdf_ins_in13 = "instances/comp191_v1/_13.jpg";
    private static String comp191_V1_pdf_ins_in14 = "instances/comp191_v1/_14.jpg";
    private static String comp191_V1_ins_27 = "instances/comp191_v1/_27.jpg";
    private static String comp191_V1_ins_28 = "instances/comp191_v1/_28.jpg";
    private static String ex0_V1_pdf_ins_in = "instances/example_0/ex0_v1.pdf";;
    private static String ex0_V2_pdf_ins_in = "instances/example_0/ex0_v2.pdf";;
    private static String ex0_V3_pdf_ins_in = "instances/example_0/ex0_v3.pdf";;
    private static String ex0_V4_pdf_ins_in = "instances/example_0/ex0_v4.pdf";;
    private static String ex0_V5_pdf_ins_in = "instances/example_0/ex0_v5.pdf";;
    private static String ex0_V6_pdf_ins_in = "instances/example_0/ex0_v6.pdf";;
    private static String ex0_V7_pdf_ins_in = "instances/example_0/ex0_v7.pdf";;
    private static String ex0_V8_pdf_ins_in = "instances/example_0/ex0_v8.pdf";;


    public static Bitmap getTestJpg1() {
        return getBitmapFromAssets(testJpg1FilePath);
    }

    public static Bitmap getTestJpg2() {
        return getBitmapFromAssets(testJpg2FilePath);
    }

    public static Bitmap getTestJpg3() {
        return getBitmapFromAssets(testJpg3FilePath);
    }

    public static Bitmap getTestJpgReal1() {
        return getBitmapFromAssets(testJpgReal1);
    }
    public static Bitmap getTestJpgReal2() {
        return getBitmapFromAssets(testJpgReal2);
    }




    public static Bitmap getTestJpegReal1() {
        return getBitmapFromAssets(testJpegReal1);
    }
    public static Bitmap getTestJpgBlackBack() {
        return getBitmapFromAssets(getTestJpegBlackBackgroud);
    }

    public static Bitmap getTestJpg1Marked() {
        return getBitmapFromAssets(testJpg1MarkedFilePath);
    }

    public static Bitmap getTestJpg2Marked() {
        return getBitmapFromAssets(testJpg2MarkedFilePath);
    }

    public static Bitmap getTestJpg3Marked() {
        return getBitmapFromAssets(testJpg3MarkedFilePath);
    }

    public static Bitmap getTestJpgDiagonal1() {
        return getBitmapFromAssets(getTestJpegDiagonal1);
    }

    public static Bitmap getExam50Qs() { return getBitmapFromAssets(test50Qs); }

    public static Bitmap getComp191_V1_JPG_Auth_No_Flash() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        final Bitmap bitmapFromAssets = getBitmapFromAssets(Comp191_V1_JPG_Auth_No_Flash);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getComp191_V1_PDF_Auth_No_Flash() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        final Bitmap bitmapFromAssets = getBitmapFromAssets(Comp191_V1_PDF_Auth_No_Flash);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getComp191_v1_JPG_ANS() {
        return getBitmapFromAssets(Comp191_v1_JPG_ANS);
    }

    public static Bitmap getComp191_v1_JPG_ANS_2() {
        return getBitmapFromAssets(Comp191_v1_JPG_ANS_2);
    }

    public static Bitmap getExam50QsAuth() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        final Bitmap bitmapFromAssets = getBitmapFromAssets(test50QsAuth);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getComp191_V1_ins_1() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_1));
    }
    public static Bitmap getComp191_V1_ins_2() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_2));
    }

    public static Bitmap getComp191_V1_ins_3() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_3));
    }

    public static Bitmap getComp191_V1_ins_4() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_4));
    }

    public static Bitmap getComp191_V1_ins_5() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_5));
    }

    public static Bitmap getComp191_V1_ins_6() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_6));
    }

    public static Bitmap getComp191_V1_ins_7() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_7));
    }

    public static Bitmap getComp191_V1_ins_8() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_8));
    }

    public static Bitmap getComp191_V1_ins_9() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_9));
    }

    public static Bitmap getComp191_V1_ins_in1() {
        return getBitmapFromAssets(comp191_V1_ins_in1);
    }

    public static Bitmap getCaspl182_V1_orig() {
        return transform90D(getBitmapFromAssets(caspl182_V1_orig));
    }

    public static Bitmap getComp191_V1_pdf_ins_in1() {
        return getBitmapFromPdfAsset(comp191_V1_pdf_ins_in1);
    }

    public static Bitmap getComp191_V1_ins_10() {
        return transform90D(getBitmapFromAssets(comp191_V1_pdf_ins_in10));
    }
    public static Bitmap getComp191_V1_ins_11() {
        return transform90D(getBitmapFromAssets(comp191_V1_pdf_ins_in11));
    }
    public static Bitmap getComp191_V1_ins_12() {
        return transform90D(getBitmapFromAssets(comp191_V1_pdf_ins_in12));
    }
    public static Bitmap getComp191_V1_ins_13() {
        return transform90D(getBitmapFromAssets(comp191_V1_pdf_ins_in13));
    }
    public static Bitmap getComp191_V1_ins_14() {
        return transform90D(getBitmapFromAssets(comp191_V1_pdf_ins_in14));
    }

    public static Bitmap getComp191_V1_ins_15() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_15));
    }

    public static Bitmap getComp191_V1_ins_16() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_16));
    }

    public static Bitmap getComp191_V1_ins_17() {
        return getBitmapFromAssets(comp191_V1_ins_17);
    }

    public static Bitmap getComp191_V1_ins_18() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_18));
    }

    public static Bitmap getComp191_V1_ins_19() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_19));
    }

    public static Bitmap getComp191_V1_ins_20() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_20));
    }

    public static Bitmap getComp191_V1_ins_21() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_21));
    }

    public static Bitmap getComp191_V1_ins_22() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_22));
    }

    public static Bitmap getComp191_V1_ins_23() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_23));
    }

    public static Bitmap getComp191_V1_ins_24() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_24));
    }

    public static Bitmap getComp191_V1_ins_25() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_25));
    }

    public static Bitmap getComp191_V1_ins_26() {
        return transform90D(getBitmapFromAssets(comp191_V1_ins_26));
    }

    public static Bitmap getComp191_V1_ins_27() {
        return getBitmapFromAssets(comp191_V1_ins_27);
    }

    public static Bitmap getComp191_V1_ins_28() {
        return getBitmapFromAssets(comp191_V1_ins_28);
    }

    public static Bitmap getCaspl182_V1_ins_001() {
        return getBitmapFromAssets(caspl182_V1_ins_1);
    }

    public static Bitmap getex0_V1_ins_1() {
        return getBitmapFromAssets(caspl182_V1_ins_1);
    }

    public static Bitmap get_pdf_ex0_V1_ins_in() {
        return getBitmapFromPdfAsset(ex0_V1_pdf_ins_in);
    }

    public static Bitmap exmp0_ver1() {
        return getBitmapFromPdfAsset(ex0_V1_pdf_ins_in);
    }
    public static Bitmap exmp0_ver2() {
        return getBitmapFromPdfAsset(ex0_V2_pdf_ins_in);
    }
    public static Bitmap exmp0_ver3() {
        return getBitmapFromPdfAsset(ex0_V3_pdf_ins_in);
    }
    public static Bitmap exmp0_ver4() {
        return getBitmapFromPdfAsset(ex0_V4_pdf_ins_in);
    }
    public static Bitmap exmp0_ver5() {
        return getBitmapFromPdfAsset(ex0_V5_pdf_ins_in);
    }
    public static Bitmap exmp0_ver6() {
        return getBitmapFromPdfAsset(ex0_V6_pdf_ins_in);
    }
    public static Bitmap exmp0_ver7() {
        return getBitmapFromPdfAsset(ex0_V7_pdf_ins_in);
    }
    public static Bitmap exmp0_ver8() {
        return getBitmapFromPdfAsset(ex0_V8_pdf_ins_in);
    }

    private static Bitmap getBitmapFromPdfAsset(String path) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(path);
            try {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File( absolutePath, "cacheFileAppeal.pdf");
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024 * 1024]; // or other buffer size
                    int read;
                    int i = 0;
                    while ((read = istr.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                        i++;
                    }
                    output.close();
                    ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                    Bitmap ans = new PDFVersionImageImpl.PDF2BitmapConverter().convert(parcelFileDescriptor, context);
                    return ans;

                }
            } finally {
            }
        } catch (IOException | PDFVersionImageImpl.InvalidVersionFileExcpetion e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap transform90D(Bitmap bitmapFromAssets) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );
        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public static Bitmap getTestAuthPic1Marked() {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        final Bitmap bitmapFromAssets = getBitmapFromAssets(testAuthPic1);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmapFromAssets,
                bitmapFromAssets.getWidth(),
                bitmapFromAssets.getHeight(),
                true
        );


        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    @Nullable
    private static Bitmap getBitmapFromAssets(String path) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    private static int c = 0;

    public static Bitmap getRandom() {
        int curr_c = c++;
        if (curr_c % 3 == 0) return getTestJpg1Marked();
        if (curr_c % 3 == 1) return getTestJpg2Marked();
        if (curr_c % 3 == 2) return getTestJpg3Marked();
        return null;
    }
}

