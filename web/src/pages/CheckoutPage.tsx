import { useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { ArrowLeft, CheckCircle } from 'lucide-react'
import { useListing } from '@/features/listings'
import { useInitiatePayment } from '@/features/payment'
import type { PaymentMethod } from '@/features/payment'
import { ZIMBABWE_PHONE_REGEX } from '@/lib/constants'

export default function CheckoutPage() {
  const { listingId } = useParams<{ listingId: string }>()
  const { data: listing } = useListing(listingId!)
  const { mutate, isPending, data: transaction, error } = useInitiatePayment()
  const [method, setMethod] = useState<PaymentMethod>('ECOCASH')
  const [phone, setPhone] = useState('')
  const [phoneError, setPhoneError] = useState('')
  const navigate = useNavigate()

  if (!listing) return null

  const currencyLabel = listing.currency === 'USD' ? 'US$' : 'ZiG'
  const price = Number(listing.price).toLocaleString()

  function handlePay() {
    if (!ZIMBABWE_PHONE_REGEX.test(phone)) {
      setPhoneError('Enter a valid Zimbabwean number, e.g. +263771234567')
      return
    }
    setPhoneError('')
    mutate({
      listingId: listingId!,
      sellerId: listing!.sellerId,
      amount: Number(listing!.price),
      currency: listing!.currency,
      payerPhone: phone,
      method,
    })
  }

  if (transaction) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4 pb-20">
        <div className="w-full max-w-sm rounded-2xl bg-white p-8 text-center shadow-md">
          <CheckCircle className="mx-auto mb-3 h-12 w-12 text-green-500" />
          <h2 className="text-lg font-semibold text-gray-900">Payment initiated</h2>
          <p className="mt-1 text-sm text-gray-500">
            {method === 'ECOCASH'
              ? 'Check your phone for the EcoCash USSD prompt and enter your PIN to confirm.'
              : 'Open your InnBucks app and approve the payment request.'}
          </p>
          <p className="mt-3 text-xs text-gray-400">
            Ref: {transaction.gatewayReference ?? transaction.id}
          </p>
          <button
            onClick={() => navigate('/')}
            className="mt-5 w-full rounded-lg bg-gray-900 py-2.5 text-sm font-medium text-white hover:bg-gray-800"
          >
            Back to home
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="mx-auto max-w-md px-4 py-6">
        <Link
          to={`/listings/${listingId}`}
          className="mb-4 flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800"
        >
          <ArrowLeft className="h-4 w-4" /> Back
        </Link>

        <h1 className="mb-5 text-xl font-semibold text-gray-900">Checkout</h1>

        <div className="rounded-xl bg-white p-5 shadow-sm">
          {/* Order summary */}
          <div className="mb-5 border-b border-gray-100 pb-4">
            <p className="text-sm text-gray-500">You are buying</p>
            <p className="mt-0.5 font-medium text-gray-900">{listing.title}</p>
            <p className="mt-1 text-2xl font-bold text-primary-600">
              {currencyLabel} {price}
            </p>
          </div>

          {/* Payment method */}
          <p className="mb-2 text-sm font-medium text-gray-700">Payment method</p>
          <div className="mb-4 space-y-2">
            {(['ECOCASH', 'INNBUCKS'] as PaymentMethod[]).map((m) => (
              <label
                key={m}
                className={`flex cursor-pointer items-center gap-3 rounded-lg border p-3 transition ${
                  method === m ? 'border-primary-500 bg-primary-50' : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                <input
                  type="radio"
                  name="method"
                  value={m}
                  checked={method === m}
                  onChange={() => setMethod(m)}
                  className="accent-primary-500"
                />
                <div>
                  <p className="text-sm font-medium">
                    {m === 'ECOCASH' ? 'EcoCash' : 'InnBucks'}
                  </p>
                  <p className="text-xs text-gray-400">
                    {m === 'ECOCASH' ? 'USSD push to your phone' : 'Approve in InnBucks app'}
                  </p>
                </div>
              </label>
            ))}
          </div>

          {/* Phone number */}
          <div className="mb-4">
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Mobile money number
            </label>
            <input
              type="tel"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="+263771234567"
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
            />
            {phoneError && <p className="mt-1 text-xs text-red-600">{phoneError}</p>}
            <p className="mt-1 text-xs text-gray-400">
              The number registered with {method === 'ECOCASH' ? 'EcoCash' : 'InnBucks'}
            </p>
          </div>

          {/* API error */}
          {error && (
            <p className="mb-3 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
              {(error as { response?: { data?: { message?: string } } }).response?.data?.message ??
                'Payment failed. Please try again.'}
            </p>
          )}

          <button
            disabled={isPending || !phone}
            onClick={handlePay}
            className="w-full rounded-lg bg-primary-500 py-2.5 text-sm font-medium text-white hover:bg-primary-600 disabled:opacity-50"
          >
            {isPending ? 'Processing…' : `Pay ${currencyLabel} ${price}`}
          </button>
        </div>
      </div>
    </div>
  )
}
