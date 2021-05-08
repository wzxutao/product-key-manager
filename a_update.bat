for /f "tokens=1-3*" %%a in ("%*") do (
    set src=%%a
    set dst=%%b
)

mkdir %dst%"/newEmptyFolder"
robocopy %dst%"/newEmptyFolder" %dst%"/src" /MIR
rmdir %dst%"/newEmptyFolder"
rmdir %dst%"/src"

robocopy %src% %dst% /E
echo Update Successful
run.bat
