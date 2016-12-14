package com.ningkangyuan.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ningkangyuan.MyApplication;

/**
 *  " + getResources().getString(R.string.DetectionKPI_java_1)
 * Created by xuchun on 2016/8/24.
 */
public class DetectionKPI implements Serializable {
    private String id;

    private String kpi_code;

    private String inspect_time;

    private String card_code;

    private String inspect_value;

    //0：normal -1：low 1：high
    private String inspect_is_normal;

    private String create_time;

    private String inspect_code;

    private String inspect_name;

    private String inspect_desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKpi_code() {
        return kpi_code;
    }

    public void setKpi_code(String kpi_code) {
        this.kpi_code = kpi_code;
    }

    public String getInspect_time() {
        return inspect_time;
    }

    public void setInspect_time(String inspect_time) {
        this.inspect_time = inspect_time;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getInspect_value() {
        return inspect_value;
    }

    public void setInspect_value(String inspect_value) {
        this.inspect_value = inspect_value;
    }

    public String getInspect_is_normal() {
        return inspect_is_normal;
    }

    public void setInspect_is_normal(String inspect_is_normal) {
        this.inspect_is_normal = inspect_is_normal;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getInspect_code() {
        return inspect_code;
    }

    public void setInspect_code(String inspect_code) {
        this.inspect_code = inspect_code;
    }

    public String getInspect_name() {
        return inspect_name;
    }

    public void setInspect_name(String inspect_name) {
        this.inspect_name = inspect_name;
    }

    public String getInspect_desc() {
        if (MyApplication.mContext.getResources().getConfiguration().locale.getCountry().equals("CN") ) {
            Iterator<Map.Entry<String, String>> iter = cnMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (inspect_desc.contains(entry.getKey())) {
                    return inspect_desc.replace(entry.getKey(), entry.getValue());
                }
            }
        } else {
            Iterator<Map.Entry<String, String>> iter = cnMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (inspect_desc.contains(entry.getValue())) {
                    return inspect_desc.replace(entry.getValue(), entry.getKey());
                }
            }
        }
        return inspect_desc;
    }

    private Map<String, String> cnMap;

    public DetectionKPI() {
        super();
        cnMap = new HashMap<>();
        cnMap.put("systolic pressure", "收缩压");
        cnMap.put("diastolic pressure", "舒张压");
        cnMap.put("Pulse rate", "脉率");
        cnMap.put("Random blood glucose", "随机血糖");
        cnMap.put("Pre meal blood glucose", "餐前血糖");
        cnMap.put("Postprandial blood glucose", "餐后血糖");
        cnMap.put("height", "身高");
        cnMap.put("weight", "体重");
        cnMap.put("temperature", "体温");
        cnMap.put("Oxygen", "血氧");
        cnMap.put("Pulse rate 2", "脉率2");
        cnMap.put("white blood cell", "白细胞");
        cnMap.put("nitrite", "亚硝酸盐");
        cnMap.put("Degree of acidity and alkalinity", "酸碱度");
        cnMap.put("Glucose", "葡萄糖");
        cnMap.put("white blood cell", "白细胞");
        cnMap.put("Ketone", "酮体");
        cnMap.put("Protein", "蛋白质");
        cnMap.put("Vitamin", "维生素");
        cnMap.put("proportion", "比重");
        cnMap.put("bilirubin", "胆红素");
        cnMap.put("Urinary bladder", "尿胆原");
    }

    public void setInspect_desc(String inspect_desc) {
        this.inspect_desc = inspect_desc;
    }
}