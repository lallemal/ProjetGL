`> [5, 0] Program
   +> ListDeclClass [List with 0 elements]
   `> [5, 0] Main
      +> ListDeclVar [List with 1 elements]
      |  []> [6, 8] DeclVar
      |      +> [6, 4] Identifier (int)
      |      +> [6, 8] Identifier (x)
      |      `> [6, 12] Initialization
      |         `> [6, 12] Int (4)
      `> ListInst [List with 1 elements]
         []> [7, 4] IfThenElse
             +> [7, 8] Lower
             |  +> [7, 8] Identifier (x)
             |  `> [7, 12] Int (5)
             +> ListInst [List with 1 elements]
             |  []> [8, 8] While
             |      +> [8, 15] Greater
             |      |  +> [8, 15] Identifier (x)
             |      |  `> [8, 19] Int (0)
             |      `> ListInst [List with 2 elements]
             |         []> [9, 12] Assign
             |         ||  +> [9, 12] Identifier (x)
             |         ||  `> [9, 16] Minus
             |         ||     +> [9, 16] Identifier (x)
             |         ||     `> [9, 20] Int (1)
             |         []> [10, 12] Print
             |             `> ListExpr [List with 1 elements]
             |                []> [10, 18] Identifier (x)
             `> ListInst [List with 1 elements]
                []> [13, 4] IfThenElse
                    +> [13, 13] Greater
                    |  +> [13, 13] Identifier (x)
                    |  `> [13, 17] Int (6)
                    +> ListInst [List with 1 elements]
                    |  []> [14, 8] Println
                    |      `> ListExpr [List with 1 elements]
                    |         []> [14, 16] Identifier (x)
                    `> ListInst [List with 1 elements]
                       []> [17, 8] Print
                           `> ListExpr [List with 1 elements]
                              []> [17, 14] StringLiteral (OUAH)
