package fr.edencraft.edentrade.content.trade;

public class CustomTrade {

    public final static String CONTENT = """
            requierments:
              # Needed items.
              items:
                '1':
                  item: "itemsadder:iasurvival__ruby"
                  # material: leather
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
                  item: "itemsadder:iasurvival__bloodnite_sword"
                  # material: diamond
                  # model-id: 10000
                  amount: 1
                '2':
                  command: 'say Hello {player} !'
                  # Amount of slot used by the command
                  amount: 0
              # Permissions to give.
              permissions:
              - "permisssion.custom.traded"
            """;
}
