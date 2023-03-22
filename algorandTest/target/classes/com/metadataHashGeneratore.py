import base64
import hashlib

extra_metadata_base64 = "iHcUslDaL/jEM/oTxqEX++4CS8o3+IZp7/V5Rgchqwc="
extra_metadata = base64.b64decode(extra_metadata_base64)
json_metadata = """{
    "name": "Sif",
    "description": "Lorem ipsum...",
    "image": "https://gateway.pinata.cloud/ipfs/QmPkXhnfNWPHVf6ZXj1JmZrfCovnWFXRExej8c2LwEth2Z",
    "image_integrity": "sha256-nxrRksRaGe/ugNwJkYVdwxTaK3jbvapwtg0VDshhF6A=",
    "image_mimetype": "image/png"
}"""

h = hashlib.new("sha512_256")
h.update(b"arc0003/amj")
h.update(json_metadata.encode("utf-8"))
json_metadata_hash = h.digest()

h = hashlib.new("sha512_256")
h.update(b"arc0003/am")
h.update(json_metadata_hash)
h.update(extra_metadata)
am = h.digest()

print("Asset metadata in base64: ")
print(base64.b64encode(am).decode("utf-8"))