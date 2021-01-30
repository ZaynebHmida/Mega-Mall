package com.anypli.megamall.ar.core.helpers;

import android.opengl.Matrix;
import android.util.Log;

public class MatrixHelper {

    public static void getProjectionMatrix(float[] outMatrix ,float fov, float ratio, float near , float far)// create 4x4 perspective matrix for projection
    {
       if(outMatrix==null || outMatrix.length <16)
           return ;
       Matrix.perspectiveM(outMatrix,0,fov,ratio,near,far);
    }
    public static void getViewMatrix (float[] outMatrix ,float[] rotationMatrix ,float x,float y , float z)//takes 3x3 rotation matrix and transform into 4x4 matrix usable by opengl
    {
        if(outMatrix==null || outMatrix.length<16)
            return ;
        float[] mat=new float[16];
        Matrix.setIdentityM(mat, 0);

        mat[0]=rotationMatrix[0];
        mat[1]=rotationMatrix[4];
        mat[2]=rotationMatrix[8];

        mat[4]=rotationMatrix[1];
        mat[5]=rotationMatrix[5];
        mat[6]=rotationMatrix[9];

        mat[8]=rotationMatrix[2];
        mat[9]=rotationMatrix[6];
        mat[10]=rotationMatrix[10];

        mat[12]=x;
        mat[13]=z;
        mat[14]=y;
        Matrix.invertM(outMatrix,0,mat,0);
        Log.d("Matrix View", "\n"+
        mat[0]+" "+
        mat[1]+" "+
        mat[2]+" "+
        mat[3]+" \n"+

        mat[4]+" "+
        mat[5]+" "+
        mat[6]+" "+
        mat[7]+" \n"+

        mat[8]+" "+
        mat[9]+" "+
        mat[10]+" "+
        mat[11]+" \n"+

        mat[12]+" "+
        mat[13]+" "+
        mat[14]);

    }
}
