package AICore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 77304 on 2021/5/1.
 */

public class TransformTable implements Serializable {
    private static final long serialVersionUID = -6876815156818851904L;

    public Map<Integer, TransformInfo> transformInfos = new HashMap<Integer, TransformInfo>();
    public final int INF = 0x3f3f3f3f;
    public final int hashMask = 1024 * 1024 - 1;


    public int readTransformTable(int ZobristKey, long ZobristKeyCheck, int depth, int alpha, int beta) {
        int hashIndex = ZobristKey & hashMask;
        if (transformInfos.get(hashIndex) == null) {
            return INF;
        }
        TransformInfo transformInfo = transformInfos.get(hashIndex);
        if (transformInfo.ZobristKeyCheck == ZobristKeyCheck && transformInfo.depth == depth) {
            if (transformInfo.flags == AI.hashfEXACT) {
                return transformInfo.value;
            }
            if (transformInfo.flags == AI.hashfALPHA && transformInfo.value <= alpha) {
                return alpha;
            }
            if (transformInfo.flags == AI.hashfBETA && transformInfo.value >= beta) {
                return beta;
            }
        }
        return INF;
    }

    public void saveTransformTable(int ZobristKey, long ZobristKeyCheck, int depth, int value, int hashf) {
        int hashIndex = ZobristKey & hashMask;
        transformInfos.put(hashIndex, new TransformInfo(ZobristKeyCheck, depth, value, hashf));
    }
}
