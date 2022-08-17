package fr.edencraft.edentrade.content.trade;

public class VanillaTrade {

    public final static String CONTENT = """
            requierments:
              # Needed items.
              items:
                '1':
                  # item: ""
                  material: leather
                  # model-id: 10000
                  amount: 16
                '2':
                  # item: ""
                  material: feather
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
                  # item: ""
                  material: diamond
                  # model-id: 10000
                  amount: 4
              # Permissions to give.
              permissions:
              - "permisssion.vanilla.traded"
            """;

}
