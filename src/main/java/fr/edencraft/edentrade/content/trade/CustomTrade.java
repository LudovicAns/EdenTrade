package fr.edencraft.edentrade.content.trade;

public class CustomTrade {

    public final static String CONTENT = """
            requierments:
              # Needed items.
              items:
                '1':
                  item: "itemsadder:namespace__itemname_1"
                  # material: leather
                  # model-id: 10000
                  amount: 16
                '2':
                  item: "itemsadder:namespace__itemname_2"
                  # material: feather
                  # model-id: 10000
                  amount: 8
              # Needed permissions.
              permissions:
              - "myperm.test"
              - "!myotherperm.test"
            
            result:
              # Items to give.
              items:
                '1':
                  item: "itemsadder:namespace__itemname_3"
                  # material: diamond
                  # model-id: 10000
                  amount: 4
              # Permissions to give.
              permissions:
              - "permisssion.vanilla.traded"
            """;
}
