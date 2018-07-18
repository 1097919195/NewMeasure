package com.example.gxkj.newmeasure.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class MultipartBeanWithUserData {
    public MultipartBeanWithUserData(List<ContractNumWithPartsData.Parts> parts) {
        this.parts = parts;
    }

    private List<ContractNumWithPartsData.Parts> parts;

    public List<ContractNumWithPartsData.Parts> getParts() {
        return parts;
    }

    public void setParts(List<ContractNumWithPartsData.Parts> parts) {
        this.parts = parts;
    }
}
