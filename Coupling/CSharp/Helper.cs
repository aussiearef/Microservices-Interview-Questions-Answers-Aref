
using System;

namespace CouplingRefactorExercise
{
    public static class Helper
    {
        public static void Log(string message)
        {
            Console.WriteLine($"[LOG]: {message}");
        }

        public static string FormatDate(DateTime date)
        {
            return date.ToString("yyyy-MM-dd");
        }

        public static bool IsValidEmail(string email)
        {
            return email.Contains("@");
        }
    }
}
