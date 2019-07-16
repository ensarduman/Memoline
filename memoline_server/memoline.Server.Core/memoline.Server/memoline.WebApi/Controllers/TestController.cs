using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using memoline.Business;
using Microsoft.Extensions.Configuration;

namespace memoline.WebApi.Controllers
{
    [Route("api/[controller]/[Action]")]
    [ApiController]
    public class TestController : MemolineControllerBase
    {
        public TestController(IConfiguration configuration) : base(configuration)
        {
        }

        [HttpGet]
        [HttpPost]
        public string Test()
        {
            return "OK!";
        }


        // // GET api/values
        // [HttpGet]
        // public ActionResult<IEnumerable<string>> Get()
        // {
        //     return new string[] { "value1", "value2" };
        // }

        // // GET api/values/5
        // [HttpGet("{id}")]
        // public ActionResult<string> Get(int id)
        // {
        //     return "value";
        // }

        // // POST api/values
        // [HttpPost]
        // public void Post([FromBody] string value)
        // {
        // }

        // // PUT api/values/5
        // [HttpPut("{id}")]
        // public void Put(int id, [FromBody] string value)
        // {
        // }

        // // DELETE api/values/5
        // [HttpDelete("{id}")]
        // public void Delete(int id)
        // {
        // }
    }
}
