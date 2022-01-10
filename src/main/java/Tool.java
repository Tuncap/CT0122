
public class Tool {
    private String code;
    private String toolType;
    private String brand;

    public String getCode() {
        return code;
    }

    public String getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }

    public Tool(String code, String toolType, String brand) {
        this.code = code;
        this.toolType = toolType;
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o){
        Tool tool = (Tool) o;
        return this.code.equals(tool.code) && this.toolType.equals(tool.toolType) && this.brand.equals(tool.brand);
    }
}
