package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;

/**
 * Exception raised when a contextual error is found.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class ContextualError extends LocationException {
    private static final long serialVersionUID = -8122514996569278575L;
    // For all passes
    public static final String IDENTIFIER_EXP_UNDEFINED = "This identifier is not declared in this scope"; // 0.1
    public static final String IDENTIFIER_TYPE_UNDEFINED = "This type is not defined."; // 0.2
    public static final String IDENTIFIER_TYPE_NOTTYPE = "The definition of this identifier is not a type type";  // 0.2
    // Passe 1
    public static final String PARENT_CLASS_NOT_DECLARED = "The parent class of this class was not declared before"; // 1.3
    public static final String PARENT_CLASS_NOT_CLASS = "The definition of the parent class is not a class identifier"; // 1.3
    public static final String CLASS_ALREADY_DEFINED = "This class is already defined"; // 1.3
    // Passe 2
    public static final String CLASS_NOT_IN_ENV = "The class is not initialize in types"; // 2.3
    public static final String CLASS_NOT_CLASS = "The class is not of nature Class"; // 2.3
    public static final String DECL_FIELD_VOID = "The type of a field to be declared can not be void"; // 2.5
    public static final String FIELD_PARENT_NOT_FIELD =  "The field declared exists in parent class Environnement but is not a field there"; // 2.5
    public static final String FIELD_ALREADY_DEFINED = "This field is already defined in this scope"; // 2.5 2.3
    public static final String METHOD_SAME_IDENT_NOT_METHOD = "This method has the same identifier as a not method object in super class"; // 2.7
    public static final String METHOD_REDEF_NOT_SAME_SIG = "The redefinition of this method has not the same signature as the method defined in the super class"; // 2.7
    public static final String METHOD_RETURN_TYPE_NOT_SUBTYPE_SUPER_METHOD = "The type of return of this method is not the same as the one of the herited method"; // 2.7
    public static final String METHOD_ALREADY_DEFINED_ENV = "This method is already defined in this scope"; // 2.7 2.3
    public static final String PARAM_TYPE_VOID = "A parameter can not be void type"; // 2.9
    public static final String PARAM_ALREADY_ENV_METHOD = "This parameter is already defined for this method";

    // Passe 3 Sans Objet
    public static final String DECL_VAR_VOID = "The type of a variable to be declared can not be void"; // 3.17 Can not be tested without class
    public static final String ASSIGN_NOT_COMPATIBLE = "The type of the variable and its assignation are not compatible"; // 3.28
    public static final String EXPR_CONDITION_NOT_BOOLEAN = "The ReturnType of the expression evaluated by the condition is not boolean"; // 3.29
    public static final String PRINT_EXPR_NOT_COMPATIBLE = "The type of expression to be printed is not int, float or string"; // 3.31
    public static final String OP_BINARY_NOT_COMPATIBLE = "At least one of the members of this operation is not compatible with this operator"; // 3.33
    public static final String OP_UNARY_NOT_COMPATIBLE = "The member of this unary operation is not compatible with this operator"; // 3.37
    public static final String LVALUE_IDENT_TYPE = "The nature of the left member is not of type field, parameters of variables"; // 3.67-3.69
    public static final String DEFINITION_ALREADY_IN_ENV = "The Definition of this Symbol is already in the environment"; // 3.17
    public static final String CONV_FLOAT_OPERAND_NOT_INT = "The conversion to a float can only be from an int"; // How to test this one ?
    // Passe 3 Objet
    public static final String CAST_INCOMPATIBLE = "This type is not compatible with the casted type"; // TODO
    public static final String TYPE_NOT_CLASS = "The type of the object to be instantiated is not a class";
    public static final String THIS_NOT_CLASS = "The type of the object pointed by this is not a class";
    public static final String THIS_CLASS_NULL = "The class pointed by this can not be Object class";
    public static final String SELECTION_EXPR_NOT_CLASS = "The type of expression selected is not a class";
    public static final String FIELD_PROTECTED = "You can not have access to this field in this class";
    public static final String SELECTION_CLASS_TYPE_UNDEFINED = "This class is not defined";
    public static final String SELECTION_CLASS_TYPE_NOT_CLASS = "The type of the selected object is not a class";
    public static final String CLASS_NOT_SUBCLASS_PROTECTED = "The selected class is not a subclass of the class where the selection was made";
    public static final String FIELD_NOT_OVERCLASS_PROTECTED = "The type of the object selected is not a subtype of the current class";
    public static final String RETURN_VOID = "The returnType can not be of type void";
    public static final String INSTANCE_OF_IMPOSSIBLE = "The operation InstanceOf can not be applied to those operands"; // TODO
    public static final String IDENTIFIER_NOT_METHOD = "This identifier does not correspond to a method in this scope"; // 3.72
    public static final String SIG_LIST_EXPR_NOT_MATCH = "The signature of this method does not match the number of parameters given"; // 3.74

    public static final String ARRAY_DIM_NULL = "This array is of null dimension : can be a base type";
    public static final String ARRAY_NEW_VOID = "The base Type of this array can not be void";
    public static final String ARRAY_LITERAL_NOT_SAME = "Elements of array literal are not all of the same type";
    public static final String ARRAY_LITERAL_EMPTY = "Array Literal can not be empty";
    public static final String ARRAY_SELECTION_NOT = "The selection in a index can not be in a not Array object";
    public static final String ARRAY_INDEX_NOT_INT = "The index of selection has to be an integer";
    public static final String ARRAY_SELECTION_MATCH_TYPE = "The dimension of the selection is greater than the dimension of the object";
    public static final String ARRAY_DIM_GIVEN = "The dimension of an array has to be give with an integer or nothing";
    public static final String ARRAY_NO_THEN = "It is impossible to give dimension if the previous index dimension was empty";

    public ContextualError(String message, Location location) {
        super(message, location);
    }
}
