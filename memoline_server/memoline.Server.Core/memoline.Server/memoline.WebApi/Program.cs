using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Server.Kestrel;
using Microsoft.AspNetCore.Server.IISIntegration;

namespace memoline.WebApi
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateWebHostBuilder(args).Build().Run();
        }

        public static IWebHostBuilder CreateWebHostBuilder(string[] args)
        {
            IConfigurationRoot configuration = new ConfigurationBuilder()
                .SetBasePath(AppDomain.CurrentDomain.BaseDirectory)
                .AddJsonFile("appsettings.json")
                .Build();

            var hostBuilder = WebHost.CreateDefaultBuilder(args);
            var hostURL = configuration.GetValue<string>("HostURL");

            hostBuilder
                .UseStartup<Startup>()
                .UseKestrel(options => {

                })
                .UseUrls(hostURL);


            return hostBuilder;
        }

    }
}
