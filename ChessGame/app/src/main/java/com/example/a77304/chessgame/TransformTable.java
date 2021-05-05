package com.example.a77304.chessgame;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 77304 on 2021/5/1.
 */

public class TransformTable implements Serializable {
    private static final long serialVersionUID = -6876815156818851904L;

    public Map<Integer,TransformInfo> transformInfos=new HashMap<Integer,TransformInfo>();
    public final int INF = 0x3f3f3f3f;
    public final int hashMask=1024*1024-1;


    public int readTransformTable(int ZobristKey,long ZobristKeyCheck,int depth){
        int hashIndex=ZobristKey&hashMask;
        if(transformInfos.get(hashIndex)==null){
            return INF;
        }
        TransformInfo transformInfo=transformInfos.get(hashIndex);
        if(transformInfo.ZobristKeyCheck!=ZobristKeyCheck||transformInfo.depth!=depth){
            return INF;
        }
        return transformInfo.value;
    }

    public void saveTransformTable(int ZobristKey,long ZobristKeyCheck,int depth,int value){
        int hashIndex=ZobristKey&hashMask;
        transformInfos.put(hashIndex,new TransformInfo(ZobristKeyCheck,depth,value));
    }
}
