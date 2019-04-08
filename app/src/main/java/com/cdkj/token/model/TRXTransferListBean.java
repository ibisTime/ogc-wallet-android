package com.cdkj.token.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @updateDts 2019/4/7
 */
public class TRXTransferListBean {

    /**
     * total : 8
     * rangeTotal : 8
     * data : [{"block":8161189,"hash":"f9a0ba90cc1795c57e7ce40bba5bbe241e1f2bda2e6cd4146ff5d9fc7dfc0cda","timestamp":1554625572000,"ownerAddress":"TYYd54w1DQasBYA6aJrAQjNyTxrFh9gRmM","toAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","contractType":1,"confirmed":true,"contractData":{"amount":10000,"owner_address":"TYYd54w1DQasBYA6aJrAQjNyTxrFh9gRmM","to_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8160220,"hash":"b8859d357db31687801c0b53114311816d8bf1095ebabdff31994acdfec1d3a0","timestamp":1554622659000,"ownerAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","toAddress":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n","contractType":1,"confirmed":true,"contractData":{"amount":100000,"owner_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","to_address":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8160207,"hash":"994e2c9e7fc5e0428a2b7881f49be0acea8ea587ffd32a9eb2f2fb9b0aba7513","timestamp":1554622620000,"ownerAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","toAddress":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n","contractType":1,"confirmed":true,"contractData":{"amount":100000,"owner_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","to_address":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8160175,"hash":"ba3106ed3793994341100633c9029c8227451ebb293828f23645b0bc41860e87","timestamp":1554622524000,"ownerAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","toAddress":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n","contractType":1,"confirmed":true,"contractData":{"amount":100000,"owner_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","to_address":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8155752,"hash":"fb44818947fd6db820dd9296fecf2a90bc6ae18da70b03f1c41874774605aa2a","timestamp":1554609093000,"ownerAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","toAddress":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n","contractType":1,"confirmed":true,"contractData":{"amount":1000,"owner_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","to_address":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8155736,"hash":"178e28f2e34456d6011822077d1003127a7424cefb1cfcf35342f29192c20ebd","timestamp":1554609045000,"ownerAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","toAddress":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n","contractType":1,"confirmed":true,"contractData":{"amount":1000,"owner_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","to_address":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8050015,"hash":"5546de4e17d9065bd315ac5cb721211cdbd4a91e453955edf829d97ef7645d0c","timestamp":1554290649000,"ownerAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","toAddress":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n","contractType":1,"confirmed":true,"contractData":{"amount":10000,"owner_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","to_address":"TVMN1dewRFBUknogaozcPk5aTwXsJeXC4n"},"SmartCalls":"","Events":"","id":"","data":"","fee":""},{"block":8046355,"hash":"d5f172ae407a365efa39d3d2537d4454525c3bb2889f1b162243cb7c71b12166","timestamp":1554279639000,"ownerAddress":"TRAPVXNnpDGQRgBVjL3Tn3Cq2rjeuDDuv9","toAddress":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU","contractType":1,"confirmed":true,"contractData":{"amount":1000000,"owner_address":"TRAPVXNnpDGQRgBVjL3Tn3Cq2rjeuDDuv9","to_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU"},"SmartCalls":"","Events":"","id":"","data":"","fee":""}]
     */

    private int total;
    private int rangeTotal;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRangeTotal() {
        return rangeTotal;
    }

    public void setRangeTotal(int rangeTotal) {
        this.rangeTotal = rangeTotal;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * block : 8161189
         * hash : f9a0ba90cc1795c57e7ce40bba5bbe241e1f2bda2e6cd4146ff5d9fc7dfc0cda
         * timestamp : 1554625572000
         * ownerAddress : TYYd54w1DQasBYA6aJrAQjNyTxrFh9gRmM
         * toAddress : TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU
         * contractType : 1
         * confirmed : true
         * contractData : {"amount":10000,"owner_address":"TYYd54w1DQasBYA6aJrAQjNyTxrFh9gRmM","to_address":"TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU"}
         * SmartCalls :
         * Events :
         * id :
         * data :
         * fee :
         */

        private int block;
        private String hash;
        private long timestamp;
        private String ownerAddress;
        private String toAddress;
        private int contractType;
        private boolean confirmed;
        private ContractDataBean contractData;
        private String SmartCalls;
        private String Events;
        private String id;
        private String data;
        private String fee;

        public int getBlock() {
            return block;
        }

        public void setBlock(int block) {
            this.block = block;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getOwnerAddress() {
            return ownerAddress;
        }

        public void setOwnerAddress(String ownerAddress) {
            this.ownerAddress = ownerAddress;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public int getContractType() {
            return contractType;
        }

        public void setContractType(int contractType) {
            this.contractType = contractType;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public ContractDataBean getContractData() {
            return contractData;
        }

        public void setContractData(ContractDataBean contractData) {
            this.contractData = contractData;
        }

        public String getSmartCalls() {
            return SmartCalls;
        }

        public void setSmartCalls(String SmartCalls) {
            this.SmartCalls = SmartCalls;
        }

        public String getEvents() {
            return Events;
        }

        public void setEvents(String Events) {
            this.Events = Events;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public static class ContractDataBean {
            /**
             * amount : 10000
             * owner_address : TYYd54w1DQasBYA6aJrAQjNyTxrFh9gRmM
             * to_address : TRuiAaJBkKXjGEKwCb6VdMgve3sENpvSWU
             */

            private BigDecimal amount;
            private String owner_address;
            private String to_address;

            public BigDecimal getAmount() {
                return amount;
            }

            public void setAmount(BigDecimal amount) {
                this.amount = amount;
            }

            public String getOwner_address() {
                return owner_address;
            }

            public void setOwner_address(String owner_address) {
                this.owner_address = owner_address;
            }

            public String getTo_address() {
                return to_address;
            }

            public void setTo_address(String to_address) {
                this.to_address = to_address;
            }
        }
    }
}
