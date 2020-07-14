using Newtonsoft.Json;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DressExchangeServer
{
    class Refresh
    {
        [JsonProperty("seller_id")]
        public string Seller_id { get; set; }
        [JsonProperty("dress_id")]
        public string Dress_id { get; set; }
        [JsonProperty("size")]
        public string Size { get; set; }
        [JsonProperty("title")]
        public string title { get; set; }
        [JsonProperty("condition")]
        public string condition { get; set; }
        [JsonProperty("suburb")]
        public string suburb { get; set; }
    }

    class Combined
    {
        [JsonProperty("dresses")]
        public ArrayList Dresses { get; set; }
        [JsonProperty("likes")]
        public string Likes { get; set; }
    }
}
