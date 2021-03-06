#!/usr/bin/env python

#-------------------------------------------------------------------------------
# Name:        logical_expression
# Purpose:     Contains logical_expression class, inference engine,
#              and assorted functions
#
# Created:     09/25/2011
# Last Edited: 07/22/2013  
# Notes:       *This contains code ported by Christopher Conly from C++ code
#               provided by Dr. Vassilis Athitsos
#              *Several integer and string variables are put into lists. This is
#               to make them mutable so each recursive call to a function can
#               alter the same variable instead of a copy. Python won't let us
#               pass the address of the variables, so put it in a list which is
#               passed by reference. We can also now pass just one variable in
#               the class and the function will modify the class instead of a
#               copy of that variable. So, be sure to pass the entire list to a
#               function (i.e. if we have an instance of logical_expression
#               called le, we'd call foo(le.symbol,...). If foo needs to modify
#               le.symbol, it will need to index it (i.e. le.symbol[0]) so that
#               the change will persist.
#              *Written to be Python 2.4 compliant for omega.uta.edu
#-------------------------------------------------------------------------------

import sys
from copy import copy
import copy
#-------------------------------------------------------------------------------
# Begin code that is ported from code provided by Dr. Athitsos
class logical_expression:
    """A logical statement/sentence/expression class"""
    # All types need to be mutable, so we don't have to pass in the whole class.
    # We can just pass, for example, the symbol variable to a function, and the
    # function's changes will actually alter the class variable. Thus, lists.
    def __init__(self):
        self.symbol = ['']
        self.connective = ['']
        self.subexpressions = []


def print_expression(expression, separator):
    """Prints the given expression using the given separator"""
    if expression == 0 or expression == None or expression == '':
        print '\nINVALID\n'

    elif expression.symbol[0]: # If it is a base case (symbol)
        sys.stdout.write('%s' % expression.symbol[0])

    else: # Otherwise it is a subexpression
        sys.stdout.write('(%s' % expression.connective[0])
        for subexpression in expression.subexpressions:
            sys.stdout.write(' ')
            print_expression(subexpression, '')
            sys.stdout.write('%s' % separator)
        sys.stdout.write(')')


def read_expression(input_string, counter=[0]):
    """Reads the next logical expression in input_string"""
    # Note: counter is a list because it needs to be a mutable object so the
    # recursive calls can change it, since we can't pass the address in Python.
    result = logical_expression()
    length = len(input_string)
    while True:
        if counter[0] >= length:
            break

        if input_string[counter[0]] == ' ':    # Skip whitespace
            counter[0] += 1
            continue

        elif input_string[counter[0]] == '(':  # It's the beginning of a connective
            counter[0] += 1
            read_word(input_string, counter, result.connective)
            read_subexpressions(input_string, counter, result.subexpressions)
            break

        else:  # It is a word
            read_word(input_string, counter, result.symbol)
            break
    return result


def read_subexpressions(input_string, counter, subexpressions):
    """Reads a subexpression from input_string"""
    length = len(input_string)
    while True:
        if counter[0] >= length:
            print '\nUnexpected end of input.\n'
            return 0

        if input_string[counter[0]] == ' ':     # Skip whitespace
            counter[0] += 1
            continue

        if input_string[counter[0]] == ')':     # We are done
            counter[0] += 1
            return 1

        else:
            expression = read_expression(input_string, counter)
            subexpressions.append(expression)


def read_word(input_string, counter, target):
    """Reads the next word of an input string and stores it in target"""
    word = ''
    while True:
        if counter[0] >= len(input_string):
            break

        if input_string[counter[0]].isalnum() or input_string[counter[0]] == '_':
            target[0] += input_string[counter[0]]
            counter[0] += 1

        elif input_string[counter[0]] == ')' or input_string[counter[0]] == ' ':
            break

        else:
            print('Unexpected character %s.' % input_string[counter[0]])
            sys.exit(1)


def valid_expression(expression):
    """Determines if the given expression is valid according to our rules"""
    if expression.symbol[0]:
        return valid_symbol(expression.symbol[0])

    if expression.connective[0].lower() == 'if' or expression.connective[0].lower() == 'iff':
        if len(expression.subexpressions) != 2:
            print('Error: connective "%s" with %d arguments.' %
                        (expression.connective[0], len(expression.subexpressions)))
            return 0

    elif expression.connective[0].lower() == 'not':
        if len(expression.subexpressions) != 1:
            print('Error: connective "%s" with %d arguments.' %
                        (expression.connective[0], len(expression.subexpressions)))
            return 0

    elif expression.connective[0].lower() != 'and' and \
         expression.connective[0].lower() != 'or' and \
         expression.connective[0].lower() != 'xor':
        print('Error: unknown connective %s.' % expression.connective[0])
        return 0

    for subexpression in expression.subexpressions:
        if not valid_expression(subexpression):
            return 0
    return 1


def valid_symbol(symbol):
    """Returns whether the given symbol is valid according to our rules."""
    if not symbol:
        return 0

    for s in symbol:
        if not s.isalnum() and s != '_':
            return 0
    return 1

# End of ported code
#-------------------------------------------------------------------------------

# Add all your functions here

def retrieve_symbols(symbols, kb =None, statement = None):
    if kb is not None:
        if kb.symbol[0]:
            symbols.append(kb.symbol[0])
        for i in kb.subexpressions:
            retrieve_symbols(symbols,i)
    if statement is not None:
        if statement.symbol[0]:
            symbols.append(statement.symbol[0])
        for i in statement.subexpressions:
            retrieve_symbols(symbols,i)

    return symbols

def eval(expression,model):
    if expression.connective[0]=='and':
        for i,sub in enumerate(expression.subexpressions):
            if i==0:
                val=eval(sub,model)
                continue
            val=val and eval(sub,model)
        return val
    elif expression.connective[0]=='or':
        for i,sub in enumerate(expression.subexpressions):
            if i==0:
                val=eval(sub,model)
                continue
            val=val or eval(sub, model)
        return val
    elif expression.connective[0]=='not':
        return not eval(expression.subexpressions[0],model)
    elif expression.connective[0]=='xor':
        for i, sub in enumerate(expression.subexpressions):
            if i==0:
                val=eval(sub, model)
                continue
            val=(val and not eval(sub,model)) or (not val and eval(sub,model))
        return val
    elif expression.connective[0]=='if':
        return  (not eval(expression.subexpressions[0],model)) or eval(expression.subexpressions[1],model)
    elif expression.connective[0] == 'iff':
        return (eval(expression.subexpressions[0],model) and eval(expression.subexpressions[1],model))
     
    return model[expression.symbol[0]]    

def retrieve_stmt_res(stmt1, stmt2):
    if stmt1 and not stmt2:
        return 'Definitely True.'
    elif not stmt1 and stmt2:
        return "Definitely False."
    elif not stmt1 and not stmt2:
        return "Possibly True, Possibly False."
    else:
        return "Both True and False."    

def res(result):
    output_file = open('result.txt','w')
    output_file.write(result)
    output_file.close()


        
def extend(symbol, value, model):
    model[symbol] = value
    return model

def check_true_false(knowledgebase, statement, model):
    symbols = []
    negation = '(not '+ statement + ')'

    # Convert statement into a negative logical stament and verify it is valid
    statement = read_expression( statement )
    if not valid_expression(statement):
        sys.exit('invalid statement')

    # Show us what the statement is
    print '\nChecking statement: ',
    print_expression(statement, '')
    print
    
    #getting symbols.
    symbols = retrieve_symbols(symbols, knowledgebase, statement)
    #elemenate the duplicates
    symbols = list(set(symbols))
    
    symbols = copy.deepcopy(symbols)
    for i in model.keys():
       symbols.remove(i)

    negative_statement = read_expression(negation,[0])

    #check ttentail for both statement and its conjugate
    stmt1 = check_TT_entail(knowledgebase, statement, symbols, model)
    stmt2 = check_TT_entail(knowledgebase, negative_statement, symbols, model)
    
    # get equivalent output string
    result =retrieve_stmt_res(stmt1, stmt2)
    # write it into result file
    print(result)
    res(result)    

def check_TT_entail(knowledge_base, statement, symbols, model):
    if not symbols:
        result = eval(knowledge_base, model)
        if result:
            result = eval(statement,model)
            return result
        else:
            return True
    else:
        symbol, remaining = symbols[0], symbols[1:]
        return check_TT_entail(knowledge_base,statement,remaining,extend(symbol,True,model)) and check_TT_entail(knowledge_base,statement,remaining,extend(symbol,False,model))

