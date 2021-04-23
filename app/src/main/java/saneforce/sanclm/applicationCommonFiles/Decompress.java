package saneforce.sanclm.applicationCommonFiles;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {
    private String _zipFile;
    private String _location;

    public Decompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;
        _dirChecker("");

    }

    public void unzip() {
        try  {
            Log.d("zip_file_un_zip ",_zipFile);
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("next_entry","inside_zin");
                if(ze.isDirectory()) {
                    _dirChecker(ze.getName());
                }
/*
                else{
                    	Log.e("Decompress_12345",  ze.getName());
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName().toString());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    Log.e("Decompress_123678",  ze.getName());
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1){
                        bufout.write(buffer, 0, read);
                    }
                    bufout.close();
                    zin.closeEntry();
                    fout.close();
                }
*/
            }
            zin.close();
            fin = new FileInputStream(_zipFile);
            zin = new ZipInputStream(fin);

            while ((ze = zin.getNextEntry()) != null) {
                Log.v("next_entry","inside_zin");
                if(ze.isDirectory()) {
                    //_dirChecker(ze.getName());
                }else{
                    Log.e("Decompress_12345",  ze.getName());
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName().toString());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    Log.e("Decompress_123678",  ze.getName());
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zin.read(buffer)) != -1){
                        bufout.write(buffer, 0, read);
                    }
                    bufout.close();
                    zin.closeEntry();
                    fout.close();
                }
            }

            zin.close();
//            Log.d("Unzip", "Unzipping complete. path :  " +_location + ze.getName());
        } catch(Exception e) {
            Log.e("Decompress_in", "unzip"+ e.getMessage()+" lightt ");
            Log.d("Unzip", "Unzipping failed");
        }
    }

    private void _dirChecker(String dir) {

        File f = new File(_location + dir);
        if(!f.isDirectory()) {
            Log.e("file_Dir", f.toString());
            f.mkdirs();
        }
    }

}
