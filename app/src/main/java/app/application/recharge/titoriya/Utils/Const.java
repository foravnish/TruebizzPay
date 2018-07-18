package app.application.recharge.titoriya.Utils;

/**
 * Created by user on 8/22/2017.
 */

public class Const {
    String name,account,type,ifsc,info, status;
    String ac, code;

        public Const(String name){
            this.name=name;
//            this.code=code;
//            this.account=account;
//            this.type=type;
//            this.ifsc=ifsc;
//            this.trtype=trtype;
//            this.status=status;

        }

    public Const(String name, String ac, String code, String info){
        this.name=name;
            this.ac=ac;
            this.code=code;
            this.info=info;
//            this.ifsc=ifsc;
//            this.trtype=trtype;
//            this.status=status;

    }

    public String getInfo() {
        return info;
    }

    public String getAc() {
        return ac;
    }


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getStatus() {
        return status;
    }
}
