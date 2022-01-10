import java.util.Arrays;

public enum ToolType {
    CHAINSAW("Chainsaw"),
    LADDER("Ladder"),
    JACKHAMMER("Jackhammer");

    private String code;

    ToolType(String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public static ToolType fromDescription(String description){
        for(ToolType toolType : ToolType.values()){
            if(toolType.getCode().equals(description)){
                return toolType;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a valid tool", description));
    }
}
