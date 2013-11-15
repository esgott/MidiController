using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using hu.midicontroller.protocol;

namespace LeapCsharpDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            TcpClient client = null;
            NetworkStream networkStrem = null;
            try
            {
                client = new TcpClient("localhost", 35678);
                Console.WriteLine("Connected to server");
                networkStrem = client.GetStream();
                while (true)
                {
                    FingersPosition message = FingersPosition.CreateBuilder().MergeDelimitedFrom(networkStrem).Build();
                    foreach (FingersPosition.Types.Finger finger in message.FingersList)
                    {
                        int position = finger.FingerPosition;
                        bool tap = finger.TapHappened;
                        Console.Write(position + " " + tap + " ");
                    }
                    Console.WriteLine();
                }
            }
            catch(Exception e)
            {
                Console.WriteLine(e.Message);
            }
            finally
            {
                networkStrem.Close();
                client.Close();
            }
        }
    }
}
