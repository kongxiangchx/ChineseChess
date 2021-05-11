package AICore;

import java.io.Serializable;

/**
 * Created by 77304 on 2021/4/30.
 */

public class TransformInfo implements Serializable {
    private static final long serialVersionUID = -4301157684495562556L;

    long ZobristKeyCheck;
    int depth;
    int value;
    int flags;

    public TransformInfo(long ZobristKeyCheck, int depth, int value, int flags) {
        this.ZobristKeyCheck = ZobristKeyCheck;
        this.depth = depth;
        this.value = value;
        this.flags = flags;
    }
}
