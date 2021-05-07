for /f "tokens=1-3*" %%a in ("%*") do (
    set src=%%a
    set dst=%%b
)

robocopy %src% %dst% /E
echo Update Successful
run.bat
