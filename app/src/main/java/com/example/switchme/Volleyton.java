package com.example.switchme;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Volleyton {
    private RequestQueue queue;
    private Context ctx;
    private  static Volleyton M_Instance;

    public Volleyton(Context Context){
        ctx=Context;
        queue=getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(queue==null){
            queue= Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return queue;
    }
    public static synchronized Volleyton getInstance(Context context){
        if (M_Instance==null){
            M_Instance=new Volleyton(context);
        }
        return M_Instance;
    }

    public <T> void addToRequestQueue(Request<T> request){
        queue.getCache().clear();
        queue.add(request);
    }
}

