`> [4, 0] Program
   +> ListDeclClass [List with 0 elements]
   `> [4, 0] Main
      +> ListDeclVar [List with 3 elements]
      |  []> [5, 8] DeclVar
      |  ||  +> [5, 4] Identifier (int)
      |  ||  +> [5, 8] Identifier (x)
      |  ||  `> NoInitialization
      |  []> [5, 10] DeclVar
      |  ||  +> [5, 4] Identifier (int)
      |  ||  +> [5, 10] Identifier (y)
      |  ||  `> NoInitialization
      |  []> [5, 12] DeclVar
      |      +> [5, 4] Identifier (int)
      |      +> [5, 12] Identifier (z)
      |      `> [5, 16] Initialization
      |         `> [5, 16] Int (1)
      `> ListInst [List with 8 elements]
         []> [6, 4] IfThenElse
         ||  +> [6, 7] Equals
         ||  |  +> [6, 7] Identifier (x)
         ||  |  `> [6, 12] Int (32)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [9, 4] IfThenElse
         ||  +> [9, 7] Equals
         ||  |  +> [9, 7] Int (32)
         ||  |  `> [9, 13] Identifier (x)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [12, 4] IfThenElse
         ||  +> [12, 7] NotEquals
         ||  |  +> [12, 7] Identifier (x)
         ||  |  `> [12, 12] Int (32)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [15, 4] IfThenElse
         ||  +> [15, 7] NotEquals
         ||  |  +> [15, 7] Int (32)
         ||  |  `> [15, 13] Identifier (x)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [18, 4] IfThenElse
         ||  +> [18, 7] GreaterOrEqual
         ||  |  +> [18, 7] Identifier (x)
         ||  |  `> [18, 12] Int (32)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [21, 4] IfThenElse
         ||  +> [21, 7] GreaterOrEqual
         ||  |  +> [21, 7] Int (32)
         ||  |  `> [21, 13] Identifier (x)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [24, 4] IfThenElse
         ||  +> [24, 7] LowerOrEqual
         ||  |  +> [24, 7] Identifier (x)
         ||  |  `> [24, 12] Int (32)
         ||  +> ListInst [List with 0 elements]
         ||  `> ListInst [List with 0 elements]
         []> [27, 4] IfThenElse
             +> [27, 7] LowerOrEqual
             |  +> [27, 7] Int (32)
             |  `> [27, 13] Identifier (x)
             +> ListInst [List with 0 elements]
             `> ListInst [List with 0 elements]
