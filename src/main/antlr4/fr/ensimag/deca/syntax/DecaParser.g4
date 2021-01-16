parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type 
    ( LHOOK (INT)? RHOOK // Délcaration d'un tableau
    | LHOOK RHOOK LHOOK RHOOK) // Déclaration d'une matrice
    list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
        $l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
            AbstractInitialization init;
        }
    : i=ident {
    	//pas de setLocation pour NoInitialization car cest une feuille
    	init = new NoInitialization();

        }
      (EQUALS e=expr {
      	init = new Initialization($e.tree);
      	setLocation(init, $e.start);
      	
        }
      )? {
      	$tree = new DeclVar($t, $i.tree, init);
      	setLocation($tree, $i.start);
        }
      | e=decl_var_array[$t] {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
      | e=decl_var_matrix[$t] {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    ;
decl_var_array[AbstractIdentifier t] returns[AbstractDeclVar tree]
	@init   {
            AbstractInitialization init;
            AbastractInteger intMemory;
        }
    : i=ident LHOOK{
    	//pas de setLocation pour NoInitialization car cest une feuille
    	init = new NoInitialization();
		intMemory = new NoInteger();
        }
      (INT {
      	intMemory = new Integer(Integer.parseInt($INT.text));
      }  	
      )? RHOOK
      (EQUALS e=expr {
      	init = new Initialization($e.tree);
      	setLocation(init, $e.start);
      	
        }
      )? {
      	$tree = new DeclVarArray($t, intMemory,  $i.tree, init);
      	setLocation($tree, $i.start);
        }
	;

decl_var_matrix[AbstractIdentifier t] returns[AbstractDeclVar tree] 
	@init   {
            AbstractInitialization init;
            AbastractInteger intMemory;
        }
    : i=ident LHOOK RHOOK LHOOK RHOOK EQUALS e=expr{
      	init = new Initialization($e.tree);
      	setLocation(init, $e.start);
      	$tree = new DeclVarMatrix($t,  $i.tree, init);
      	setLocation($tree, $i.start);
        }
	;
list_inst returns[ListInst tree]
@init {
	$tree = new ListInst();
}
    : (inst {
    	$tree.add($inst.tree);

        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
            setLocation($tree, $e1.start);
        }
    | SEMI {
    	$tree = new NoOperation();
    	setLocation($tree, $SEMI);
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false, $list_expr.tree);
            setLocation($tree, $PRINT);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false, $list_expr.tree);
            setLocation($tree, $PRINTLN);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true, $list_expr.tree);
            setLocation($tree, $PRINTX);

        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true, $list_expr.tree);
            setLocation($tree, $PRINTLNX);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
            setLocation($tree, $if_then_else.start);
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree, $body.tree);
   			setLocation($tree, $WHILE);

        }
    | RETURN expr SEMI {
            assert($expr.tree != null);
            $tree = new Return($expr.tree);
            setLocation($tree, $RETURN);
        }
    ;

if_then_else returns[IfThenElse tree]
@init {
	ListInst elseNew = new ListInst();
	ListInst elseList = new ListInst();
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
    	assert($condition.tree != null);
    	assert($li_if.tree != null);
    	$tree = new IfThenElse($condition.tree, $li_if.tree, elseList);
    	setLocation($tree, $if1);
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
      	assert($elsif_cond.tree != null);
      	assert($elsif_li.tree != null);
      	IfThenElse newIfBranch = new IfThenElse($elsif_cond.tree, $elsif_li.tree, elseNew);
      	elseList.add(newIfBranch);
      	setLocation(newIfBranch, $ELSE);
      	elseList = elseNew;
      	elseNew = new ListInst();
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
      	assert($li_else.tree != null);
      	for (AbstractInst inst : $li_else.tree.getList()) {
            elseList.add(inst);
        }
        }
      )?
    ;

list_expr returns[ListExpr tree]
@init   {

	$tree = new ListExpr();
        }
    : (e1=expr {
    	$tree.add($e1.tree);
        }
       (COMMA e2=expr {
       	$tree.add($e2.tree);
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : assign_expr {
            assert($assign_expr.tree != null);
            $tree = $assign_expr.tree;
            setLocation($tree, $assign_expr.start);
        }
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
        }
        EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue)$e.tree, $e2.tree);
            setLocation($tree, $e.start);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
        	$tree = $e.tree;
        	setLocation($tree, $e.start);
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);                         
            assert($e2.tree != null);
            $tree = new And($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
            $tree = new InstanceOf($e1.tree, $type.tree);
            setLocation($tree, $e1.start);            
        }
    ;


sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);                                         
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);                                                                          
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $e.start);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $e.start);
        }
    | select_expr {
            assert($select_expr.tree != null);
            $tree = $select_expr.tree;
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree = $e.tree;
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
            $tree = new Selection($e1.tree, $i.tree);
            setLocation($tree, $e1.start);
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree);
            setLocation($tree, $e1.start);
        }
        | /* epsilon */ {
            // we matched "e.i"
        }
        )
    ;

primary_expr returns[AbstractExpr tree]
@init{
	AbstractInteger intMemory;
}
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
            $tree = new MethodCall(new This(), $m.tree, $args.tree);
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
        }
    | READINT OPARENT CPARENT {
    		$tree = new ReadInt();
    		setLocation($tree, $READINT);
    		
        }
    | READFLOAT OPARENT CPARENT {
    		$tree = new ReadFloat();
    		setLocation($tree, $READFLOAT);
        }
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $NEW); 
            
        }
     | NEW ident LHOOK i1=INT RHOOK  LHOOK{ // Déclaration d'une matrice
	    	assert($ident.tree != null);
	    	intMemory = new NoInteger();
	    }  (i2=INT {
	    	intMemory = new Integer(Integer.parseInt($i2.text));

	    })? RHOOK{
	    	$tree = new NewMatrix($ident.tree,  Integer.parseInt($i1.text), intMemory);
	    	setLocation($tree, $NEW); // Pas sur du $NEW
	    }
     | NEW ident LHOOK i1=INT RHOOK  { // Déclaration d'un tableau 
    	assert($ident.tree != null);
    	$tree = new NewMatrix($ident.tree, Integer.parseInt($INT.text));
    	setLocation($tree, $NEW); // Pas sur du $NEW
    }
    | l=list_element{ // Definition de array explicite 
    	assert($l.tree != null);
    	$tree = $l.tree;
    }
    | l=list_array{ // Definition de matrice explicite
    	assert($l.tree != null);
    	$tree = $l.tree;
    }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
            $tree = new Cast($type.tree, $expr.tree);
            setLocation($tree, $OPARENT);
        }
    | literal {
            assert($literal.tree != null);
            $tree = $literal.tree;
        }
    ;

list_element returns[ListExpr tree]
@init   {

	$tree = new ListExpr();
        }
    : OBRACE((ident{
			$tree.add($ident.tree);
			setLocation($tree, $ident.start);
		}
	) ( COMMA ident{
		$tree.add($ident.tree)
		setLocation($tree, $ident.start);
	}
		
	)*)?CBRACE
    ;


list_array returns[ListExpr tree]
@init   {

	$tree = new ListArray();
        }
    : OBRACE((
    	e=list_element{
    		$tree.add($e.tree);
    		setLocation($tree, $e.start);
    	}
	) ( COMMA e=list_element{
		$tree.add($e.tree)
		setLocation($tree, $e.start);
	}
		
	)*)?CBRACE
    ;

type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
        }
    ;

literal returns[AbstractExpr tree]
    : INT {
   			$tree = new IntLiteral(Integer.parseInt($INT.text));
   			setLocation($tree, $INT);

        }
    | fd=FLOAT {
    		$tree = new FloatLiteral(Float.parseFloat($fd.text));
   			setLocation($tree, $fd);

        }
    | STRING {
    	$tree = new StringLiteral($STRING.text);
   			setLocation($tree, $STRING);
        }
    | TRUE {
    	$tree = new BooleanLiteral(true);
   			setLocation($tree, $TRUE);
        }
    | FALSE {
    	$tree = new BooleanLiteral(false);
   			setLocation($tree, $FALSE);
        }
    | THIS {
    	$tree = new This();
        setLocation($tree, $THIS);
        }
    | NULL {
    	$tree = new Null();
        setLocation($tree, $NULL);
        }
    ;

ident returns[AbstractIdentifier tree]
    : IDENT {
    	$tree = new Identifier(getDecacCompiler().getSymbols().create($IDENT.text));
    	setLocation($tree, $IDENT);
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
@init {
        $tree = new ListDeclClass();
}
     : (c1=class_decl {
            $tree.add($c1.tree);
        }
      )*
    ;


class_decl returns[DeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
            assert($name.tree != null);
            $tree = new DeclClass($name.tree, $superclass.tree, $class_body.list_field, $class_body.list_meth);
            setLocation($tree, $name.start);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
            setLocation($tree, $EXTENDS);
        }
    | /* epsilon */ {
        }
    ;


class_body returns[ListDeclMethod list_meth, ListDeclField list_field]
@init {
       $list_meth = new ListDeclMethod();
       $list_field = new ListDeclField();
}
    : (m=decl_method {
            $list_meth.add($m.tree);
        }
      | decl_field_set[$list_field]{
      }
      )*{
      }
    ;

decl_field_set[ListDeclField l]
    : v=visibility t=type list_decl_field[$l,$t.tree, $v.vis]
      SEMI
    ;

visibility returns[Visibility vis]
    : /* epsilon */ {
        $vis = Visibility.PUBLIC;
        }
    | PROTECTED {
        $vis = Visibility.PROTECTED;
        }
    ;

list_decl_field[ListDeclField l, AbstractIdentifier t, Visibility v]
    : dv1=decl_field[$t, $v]{
        $l.add($dv1.tree);
    }
        (COMMA dv2=decl_field[$t, $v]{
        $l.add($dv2.tree);
        }
      )*
    ;

decl_field[AbstractIdentifier t, Visibility v] returns[DeclField tree]
@init {
        AbstractInitialization init;
}
    : i=ident {
        assert($i.tree != null);
        init = new NoInitialization();
        }
      (EQUALS e=expr {
        init = new Initialization($e.tree);
      	setLocation(init, $e.start);
        }
      )? {
         $tree = new DeclField($v, $t, $i.tree, init);
         setLocation($tree, $i.start);
        }
    ;


decl_method returns[DeclMethod tree]
@init {
        
}
    : type ident OPARENT params=list_params CPARENT (block {
            assert($type.tree != null);
            assert($ident.tree != null);
            $tree = new DeclMethod($type.tree, $ident.tree, $params.tree, new MethodBody($block.decls, $block.insts));
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
            assert($type.tree != null);
            assert($ident.tree != null);
            $tree = new DeclMethod($type.tree, $ident.tree, $params.tree, new MethodAsmBody(new StringLiteral($code.text)));
        }
      ) {
            setLocation($tree, $type.start);
        }
    ;

list_params returns[ListParam tree]
@init {
     $tree = new ListParam();
}
    : (p1=param {
            $tree.add($p1.tree);
        } (COMMA p2=param {
            $tree.add($p2.tree);
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[DeclParam tree] 
       : type ident {
            assert($type.tree != null);
            assert($ident.tree != null);
            $tree = new DeclParam($type.tree, $ident.tree);
            setLocation($tree, $type.start);
        }
    ;
    
/**** Array related rule  ****/
