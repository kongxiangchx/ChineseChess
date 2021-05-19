package Info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import Info.ChessInfo;

/**
 * Created by 77304 on 2021/4/6.
 */

public class InfoSet implements Serializable {
    private static final long serialVersionUID = 2194052829864919744L;

    public Stack<ChessInfo> preInfo = new Stack<ChessInfo>();
    public ChessInfo curInfo = new ChessInfo();
    public Map<Long,Integer> ZobristInfo=new HashMap<Long,Integer>(){{
        put(7691135808095748096L,1);
    }};

    public InfoSet() {

    }

    public void pushInfo(ChessInfo chessInfo) throws CloneNotSupportedException {
        ChessInfo tmp = new ChessInfo();
        tmp.setInfo(curInfo);
        preInfo.push(tmp);
        curInfo.setInfo(chessInfo);
        if(ZobristInfo.get(chessInfo.ZobristKeyCheck)==null){
            ZobristInfo.put(chessInfo.ZobristKeyCheck,1);
        }
        else{
            ZobristInfo.put(chessInfo.ZobristKeyCheck,ZobristInfo.get(chessInfo.ZobristKeyCheck)+1);
        }
    }

    public void newInfo() throws CloneNotSupportedException {
        preInfo.clear();
        curInfo.setInfo(new ChessInfo());
        ZobristInfo.clear();
        ZobristInfo.put(7691135808095748096L,1);
    }

    public void recallZobristInfo(Long ZobristCheck){
        if(ZobristInfo.get(ZobristCheck)==null){
            return ;
        }
        ZobristInfo.put(ZobristCheck,ZobristInfo.get(ZobristCheck)-1);
    }
}
