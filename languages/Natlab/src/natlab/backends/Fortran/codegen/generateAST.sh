cd ~/Project/languages/Natlab/src
rm -rf FortranAST
java -jar natlab/backends/Fortran/codegen/jastadd2.jar --package=natlab.backends.Fortran.codegen.FortranAST natlab/backends/Fortran/codegen/FortranIR.ast
