package br.com.doit.rxworld.orchestration.product;

import javax.json.bind.JsonbBuilder;

public class DOitProductCommand {
    public static DOitProductCommand fromJson(String json) {
        var jsonb = JsonbBuilder.create();

        return jsonb.fromJson(json, DOitProductCommand.class);
    }

    public String system;
    public Integer webStore;
    public String sku;
    public String flow;
    public Integer subWebStoreId;
	public String storeView;
}