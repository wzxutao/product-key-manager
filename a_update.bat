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
REM pseudo sleep
ping 127.0.0.1 -n 4 > nul
run.bat
